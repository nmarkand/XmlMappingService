var xmlMappingTesting = (function () {
	
	return {
		init : function() {
			console.log("xmlMappingTesting.init");
			if(location.href.includes('?')) {
				xmlMappingTesting.setSelectedTemplate(xmlMappingUtil.getUrlParams(location.href));
				xmlMappingTesting.actionRunForTemplate();
			}
		},
		
		actionRunForTemplate : function() {
			console.log("xmlMappingTesting.actionRunForTemplate");
			var runData = {
					templateIdentifier: xmlMappingTesting.getSelectedTemplate(),
					testParams : xmlMappingTesting.inpGetTestRunParams()
			}
			console.log("xmlMappingTesting.runForTemplate",runData);
			xmlMappingUtil.postJson("testsuite/runForTemplate",runData,function(data) { xmlMappingTesting.onTestsuiteRun(data); });
		},
		
		onTestsuiteRun : function(data) {
			console.log("xmlMappingTesting.onTestsuiteRun",data);
			xmlMappingTesting.displayTestsuiteResults(data);
		},
		
		actionSaveTest : function() {
			console.log("xmlMappingTesting.actionSaveTest");
			var saveData = {
					templateIdentifier: xmlMappingTesting.getSelectedTemplate(),
					source : $('#txt_test_source').val(),
					target : $('#txt_test_target').val()
			}
			console.log("xmlMappingTesting.saveTest",saveData);
			xmlMappingUtil.postJson("testsuite/storeTest",saveData,function(data) { xmlMappingTesting.onTestStored(data); });
		},
		
		onTestStored : function(data) {
			console.log("xmlMappingTesting.onTestStored",data);
			xmlMappingTesting.actionRunForTemplate();
		},
		
		actionShowAndRunTest : function(btn) {
			console.log("xmlMappingTesting.actionShowAndRunTest");
			var testId = xmlMappingTesting.getEventRowTestId(btn)
			xmlMappingTesting.loadTestContent(testId);
			xmlMappingTesting.runTest(testId);
		},
		
		loadTestContent : function(testId) {
			console.log("xmlMappingTesting.loadTestContent",testId);
			xmlMappingUtil.getJson("testsuite/test/content/" + testId,function(data) { xmlMappingTesting.onTestContentLoaded(data); });
		},
		
		onTestContentLoaded : function(data) {
			console.log("xmlMappingTesting.onTestContentLoaded",data);
			xmlMappingTesting.displayTestContent(data);
		},
		
		runTest : function(testId) {
			console.log("xmlMappingTesting.runTest",testId);
			xmlMappingUtil.postJson("testsuite/test/run/" + testId, xmlMappingTesting.inpGetTestRunParams(), function(data) { xmlMappingTesting.onTestRun(data); });
		},
		
		onTestRun : function(data) {
			console.log("xmlMappingTesting.onTestRun",data);
			xmlMappingTesting.displayTestRunResult(data);
			if(data.outcome == 'ERROR') {
				alert(data.errorMessage);
				return;
			}
		},
		
		actionDeleteTest : function(btn) {
			console.log("xmlMappingTesting.actionDeleteTest");
			var testId = xmlMappingTesting.getEventRowTestId(btn)
			xmlMappingUtil.getJson("testsuite/test/delete/" + testId,function(data) { xmlMappingTesting.onTestDeleted(data); });
		},
		
		onTestDeleted : function(data) {
			console.log("xmlMappingTesting.deleteTest",data);
			xmlMappingTesting.actionRunForTemplate();
		},
		
		actionClearTest : function() {
			console.log("xmlMappingTesting.actionClearTest");
			$('#txt_test_source').val("");
			$('#txt_test_target').val("");
			$('#txt_test_result_transformed').val("");
		},
		
		getSelectedTemplate : function() {
			return {
				vendorId: $('#inp_template_vendor').val(),
				sourceType: $('#inp_template_source').val(),
				targetType: $('#inp_template_target').val(),
			}
		},
		
		setSelectedTemplate : function(templateIdentifier) {
			console.log("xmlMappingTesting.setSelectedTemplate",templateIdentifier);
			$('#inp_template_vendor').val(templateIdentifier.vendorId);
			$('#inp_template_source').val(templateIdentifier.sourceType);
			$('#inp_template_target').val(templateIdentifier.targetType);
		},
		
		getEventRowTestId : function(eventSource) {
			console.log("xmlMappingTesting.getEventRowTestId");
			return $(eventSource).closest("tr").attr('data-testId');
		},
		
		inpGetTestRunParams : function() {
			console.log("xmlMappingTesting.inpGetTestRunParams");
			return { ignoreWhitespace: $('#cb_ignore_whitespace').prop('checked') }
		},
		
		displayTestsuiteResults : function(result) {
			console.log("xmlMappingTesting.displayTestsuiteResults",result);
			$('#table_results').find("tr:gt(0)").remove();
			var i = 0;
			result.results.forEach(function(result) {
				var tr = xmlMappingUtil.tr(
						[
							result.testId,
							"<span id='txt_table_results_test_outcome_" + i + "'>" + result.outcome + "</span>",
							"<button id='btn_runTest_" + i + "'>Run</button>",
							"<button id='btn_deleteTest_" + i + "'>Delete</button>"
						]
				);
				$('#table_results').append(tr);
				$('#table_results').find("tr").last().attr('data-testId', result.testId);		
				
				$("#btn_runTest_" + i).click(function() { xmlMappingTesting.actionShowAndRunTest(this); });
				$("#btn_deleteTest_" + i).click(function() { xmlMappingTesting.actionDeleteTest(this); });
				i++;
			});	
		},
		
		displayTestContent : function(content) {
			console.log("xmlMappingTesting.displayTestContent",content);
			$('#txt_test_source').val(content.source);
			$('#txt_test_target').val(content.target);
		},
		
		displayTestRunResult : function(result) {
			console.log("xmlMappingTesting.displayTestRunResult",result);
			xmlMappingTesting.displayTestOutcome(result);
			
			$('#txt_test_result_id').html(result.testId);
			$('#txt_test_result_outcome').html(result.outcome);
			$('#txt_test_result_transformed').val(result.transformed);
			
			$('#ul_test_result_diffs').find("li").remove();
			result.diffs.forEach(function(diff) {
				console.log(diff);
				$('#ul_test_result_diffs').append("<li>" + diff + "</li>");
			});
		},
		
		displayTestOutcome : function(result) {
			console.log("xmlMappingTesting.displayTestOutcome",result);
			$('#table_results').find("tr[data-testid='" + result.testId + "']").find("span[id *= 'txt_table_results_test_outcome_']").html(result.outcome);
		}, 
	}
})();