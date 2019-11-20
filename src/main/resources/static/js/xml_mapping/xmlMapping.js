var xmlMapping = (function () {
	
	var model = {
		templateList : [],
	    currentTemplate : null
	};
	
	return {
		init : function() {
			console.log("xmlMapping.init");
			xmlMapping.registerActions();
			xmlMapping.loadTemplateList();
		},
		
		registerActions : function() {
			console.log("xmlMapping.registerActions");
			xmlMappingView.toggleSaveButton(false);
			
			$('#btn_search_template').click(function() { xmlMapping.actionSearchTemplateList()});
			$('#btn_export_template').click(function() { xmlMapping.actionExportTemplateList()});
			$('#btn_import_template').click(function() { xmlMapping.actionImportTemplateList()});
			$('#btn_reset_page').click(function() { xmlMapping.actionResetSearch()});
			$('#btn_save_template').click(function() { xmlMapping.actionStoreTemplate()});
			$('#btn_delete_template').click(function() { xmlMapping.actionDeleteCurrentTemplate()});
			$('#btn_template_versions').click(function() { xmlMapping.actionLoadCurrentTemplateVersionList()});
			$("#select_template_versions").change(function() { xmlMapping.onVersionSelected(); });
			$('#btn_test_template').click(function() { xmlMapping.actionOpenTestsuiteForCurrentTemplate()});
			$('#btn_new_template').click(function() { xmlMapping.actionNewTemplate() });
			$('#btn_transform').click(function() { xmlMapping.actionTransformWithEdit()});
			$('#txt_template_content').focus(function() { xmlMappingView.toggleSaveButton(true) });
			
			$("#table_templates").on("click","[id *= btn_loadTemplate_]",function() { xmlMapping.actionLoadTemplateContent(this); });
		},
		
		setCurrentTemplate : function(templateIdent) {
			console.log("xmlMapping.setCurrentTemplate",templateIdent);
			model.currentTemplate = templateIdent;
			xmlMappingView.displayTemplateIdent(model.currentTemplate);
		},
		
		loadTemplateList : function() {
			console.log("xmlMapping.loadTemplateList");
			xmlMappingUtil.getJson("template/list",function(data) { xmlMapping.onTemplateListLoaded(data); });
		},
		
		actionSearchTemplateList : function() {
			console.log("xmlMapping.actionSearchTemplateList");
			xmlMappingUtil.postJson("template/search",xmlMappingView.inputGetTemplateListFilter(), function(data) { xmlMapping.onTemplateListLoaded(data); });
		},
		
		actionExportTemplateList : function() {
			console.log("xmlMapping.actionExportTemplateList");
			xmlMappingUtil.postAndExport("template/export", xmlMappingView.inputGetTemplateListFilter());
		},
		
		actionImportTemplateList : function() {
			console.log("xmlMapping.actionImportTemplateList");
			xmlMappingUtil.postAndImport("template/import", xmlMappingView.inputGetTemplateFile(), function(data) { xmlMapping.onTemplateFileImport(data);});
		},

		onTemplateListLoaded : function(templates) {
			console.log("xmlMapping.onTemplateListLoaded",templates);
			model.templateList = templates;
			xmlMappingView.displayTemplates(model.templateList);
		},
		
		actionLoadTemplateContent : function(btn) {
			console.log("xmlMapping.actionLoadTemplateContent");			
			xmlMapping.doLoadTemplateContent(xmlMapping.getEventRowTemplate(btn));
		},
		
		onTemplateFileImport : function(data) {
			console.log("xmlMapping.onTemplateListLoaded", data);
            alert('Template list import successful. Total count of imported templates ' + data.count);             
		},
		
		doLoadTemplateContent : function(templateIdent) {
			console.log("xmlMapping.doLoadTemplateContent",templateIdent);
			xmlMappingUtil.postJson("template/content",templateIdent, function(data) { xmlMapping.onTemplateContentLoaded(templateIdent,data); });
		},
		
		onTemplateContentLoaded : function onTemplateContentLoaded(templateIdent,contentResult) {
			console.log("xmlMapping.onTemplateContentLoaded",templateIdent,contentResult);
			xmlMapping.setCurrentTemplate(templateIdent);
			xmlMappingView.displayTemplateContent(contentResult.content);
			xmlMappingView.toggleVersionsButtonOrDropdown(true);
		},
		
		actionLoadCurrentTemplateVersionList : function() {
			console.log("xmlMapping.actionLoadCurrentTemplateVersionList");
			xmlMappingUtil.postJson("template/version/list", model.currentTemplate, function(data) { xmlMapping.onTemplateVersionsLoaded(data); }); 
		},		
		
		onTemplateVersionsLoaded : function (data) {
			console.log("xmlMapping.onTemplateVersionsLoaded",data);
			xmlMappingView.displayTemplateVersionNumbers(data);
			xmlMapping.doSelectLatestVersionOption();	
		},
		
		doSelectLatestVersionOption : function() {
			console.log("xmlMapping.doSelectLatestVersionOption");
			var select = $('#select_template_versions');
			var latestOption = select.find("option").eq(1);
			latestOption.prop('selected', true);
			xmlMapping.doLoadVersionContent(model.currentTemplate, latestOption.val());				
		},
		
		onVersionSelected : function(){
			console.log("xmlMapping.onVersionSelected");
			var val = $('#select_template_versions').val();
			if(val == 'Current') { 
				xmlMapping.doLoadTemplateContent(model.currentTemplate);
				return;
			}
			xmlMapping.doLoadVersionContent(model.currentTemplate, $('#select_template_versions').val());		
		},
		
		doLoadVersionContent : function(templateIdentifier,version) {
			console.log("xmlMapping.doLoadVersionContent",templateIdentifier,version);
			var versionContentRequest = {
					templateIdentifier : templateIdentifier,
					versionNumber: version
			};		
			xmlMappingUtil.postJson("template/version/content",versionContentRequest, function(data) { xmlMapping.onVersionContentLoaded(data); });		
		},
		
		onVersionContentLoaded : function(data){
			console.log("xmlMapping.onVersionContentLoaded", data);			
			xmlMappingView.displayTemplateContent(data.templateContent);
		},
		
		actionStoreTemplate : function() {
			console.log("xmlMapping.actionStoreTemplate");
			var storeData = {
					templateIdentifier: model.currentTemplate,
					templateContent: xmlMappingView.inputGetTemplateContent()
			};
			xmlMappingUtil.postJson("template/store",storeData,function(data) { xmlMapping.onTemplateStored(data); });			
		},
		
		onTemplateStored: function(data) {
			console.log("xmlMapping.onTemplateStored",data);
			xmlMappingView.toggleVersionsButtonOrDropdown(true); // version need to be updated
			xmlMappingView.toggleSaveButton(false);
		},
		
		actionNewTemplate : function() {
			console.log("xmlMapping.actionNewTemplate");
			xmlMappingView.displayTemplateContent('');
			xmlMapping.setCurrentTemplate(xmlMappingView.inputGetTemplateIdentifier());
			
			var storeData = {
					templateIdentifier: model.currentTemplate,
					templateContent: 'CREATED'
			}
			xmlMappingUtil.postJson("template/create",storeData,function(data) { xmlMapping.onTemplateCreated(data); });
		},
		
		onTemplateCreated: function(data) {
			console.log("xmlMapping.onTemplateCreated",data);
			xmlMapping.loadTemplateList();
		},
		
		actionTransformWithIdentifier : function() {
			console.log("xmlMapping.actionTransformWithIdentifier");
			var transformData = {
					templateIdentifier: currentTemplate,
					sourceContent: xmlMappingView.inputGetSourceContent()
			};
			xmlMappingUtil.postJson("transform",transformData,function(data) { xmlMapping.onTransformed(data); });
		},
		
		actionTransformWithEdit : function() {
			console.log("xmlMapping.actionTransformWithEdit");
			var transformData = {
					templateContent: xmlMappingView.inputGetTemplateContent(),
					sourceContent: xmlMappingView.inputGetSourceContent()
			};
			xmlMappingUtil.postJson("transformFromTemplateString",transformData,function(data) { xmlMapping.onTransformed(data); });
		},
		
		onTransformed : function(data) {
			console.log("xmlMapping.onTransformed", data);
			xmlMappingView.displayTransformed(data.transformed);
		},
		
		actionDeleteCurrentTemplate : function() {
			if(! confirm("delete?")) return;
			console.log("xmlMapping.actionDeleteCurrentTemplate");
			xmlMappingUtil.postJson("template/delete",model.currentTemplate,function(data) { xmlMapping.onTemplateDeleted(data); });
		},
		
		onTemplateDeleted: function(data) {
			console.log("xmlMapping.onTemplateDeleted",data);
			xmlMapping.loadTemplateList();
		},
		
		actionOpenTestsuiteForCurrentTemplate : function() {
			console.log("xmlMapping.actionOpenTestsuiteForCurrentTemplate");
			var templateIdent = model.currentTemplate;
			var url = "testing.html?";
			url += "vendorId=" + templateIdent.vendorId;
			url += "&sourceType=" + templateIdent.sourceType;
			url += "&targetType=" + templateIdent.targetType;
			window.open(url,'_blank');
		},
		
		actionResetSearch : function() {
			console.log("xmlMapping.actionResetSearch");
			xmlMappingView.clearSearchContent();
			xmlMapping.loadTemplateList();
		},
		
		getEventRowTemplate : function(eventSource) {
			console.log("xmlMapping.getEventRowTemplate");
			var index = $(eventSource).closest("tr").attr('data-templateIndex');
			return model.templateList[index];
		},
		
	}
})();
		
var xmlMappingView = (function () {

	return {
		
		inputGetTemplateListFilter : function() {
			console.log("xmlMappingView.inputGetTemplateListFilter");
			return {
					vendorId: $('#inp_vendor_id').val().trim() ? $('#inp_vendor_id').val(): null,
					sourceType: $('#inp_source_type').val().trim() ? $('#inp_source_type').val(): null,
					targetType: $('#inp_target_type').val().trim() ? $('#inp_target_type').val(): null
			};
		},
		
		inputGetTemplateFile : function() {
			console.log("xmlMappingView.inputGetTemplateFile");
			return $('#inp_import_template')[0].files[0];				
		},
		
		inputGetTemplateIdentifier : function() {
			console.log("xmlMappingView.inputGetTemplateIdentifier");
			return {
						vendorId: $('#inp_vendor_id').val(),
						sourceType: $('#inp_source_type').val(),
						targetType: $('#inp_target_type').val(),
					}
		},
		
		inputGetTemplateContent : function() {
			console.log("xmlMappingView.inputGetTemplateContent");
			return $('#txt_template_content').val();
		},
		
		inputGetSourceContent : function() {
			console.log("xmlMappingView.inputGetSourceContent");
			return $('#txt_source_content').val();
		},
		
		displayTemplates : function(templates) {
			console.log("xmlMappingView.displayTemplates",templates);
			$('#table_templates').find("tr:gt(0)").remove();
			let i = 0;
			templates.forEach(function(template) {
				var tr = xmlMappingUtil.tr(
						[
							template.vendorId,
							template.sourceType,
							template.targetType,
							"<button id='btn_loadTemplate_" + i + "'>Edit</button>",
						]
				);
				$('#table_templates').append(tr);
				$('#table_templates').find("tr").last().attr('data-templateIndex', i);		
				i++;
			});
			xmlMappingView.toggleListOrContentDisplay(false);
		},
		
		toggleListOrContentDisplay : function(content) {
			if(content) {
				$('#div_template_list').hide();
				$('#div_template_content').show();
			}
			else {
				$('#div_template_list').show();
				$('#div_template_content').hide();
			}
		},
		
		displayTemplateIdent : function(templateIdent) {
			console.log("xmlMappingView.displayTemplateIdent",templateIdent);
			vendorId: $('#inp_vendor_id').val(templateIdent.vendorId);
			sourceType: $('#inp_source_type').val(templateIdent.sourceType);
			targetType: $('#inp_target_type').val(templateIdent.targetType);
		},
		
		displayTemplateContent : function(content) {
			console.log("xmlMappingView.displayTemplateContent");
			$('#txt_template_content').val(content);
			xmlMappingView.toggleListOrContentDisplay(true);
			xmlMappingView.toggleSaveButton(false);
		},
		
		displayTransformed : function(transformed) {
			console.log("xmlMappingView.displayTransformed");
			$('#txt_transformed_content').val(transformed);
		},
		
		displayTemplateVersionNumbers : function(numberAndValidTo) {
			console.log("xmlMappingView.displayTemplateVersionNumbers",numberAndValidTo);
			
			var select = $('#select_template_versions')
			select.find("option").remove();
			select.append("<option>Current</option>");
			
			numberAndValidTo.forEach(function(object){				
				select.append('<option  value="' + object.versionNumber  + '">' + object.versionNumber +" "+ object.validTo + '</option>');
			});
			
			xmlMappingView.toggleVersionsButtonOrDropdown(false);			
		},
		
		toggleVersionsButtonOrDropdown : function (btn) {
			console.log("xmlMappingView.toggleVersionsButtonOrDropdown");
			if(btn) {
				$('#btn_template_versions').show();
				$('#select_template_versions').hide();
			} else {
				$('#btn_template_versions').hide();
				$('#select_template_versions').show();
			}
		},
		
		toggleSaveButton : function(enable) {
			console.log("xmlMappingView.toggleSaveButton",enable);
			$('#btn_save_template').prop("disabled",!enable);
		},
		
		clearSearchContent : function() {
			console.log("xmlMappingView.clearSearchContent");
			
			$('#inp_vendor_id').val('');
			$('#inp_source_type').val('');
			$('#inp_target_type').val('');
		},
	}
})();