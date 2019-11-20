var xmlMappingUtil = (function () {
	
	return {
		
		getJson : function(url, callback) {
			console.log("xmlMappingUtil.getJson",url);
			$.ajax({
				  url: url,
				}).done(callback)
                .fail(function(jaxhr,textStatus,error) {
                	alert("service error:" + jaxhr.responseText);
                });				
		},
		
		postJson : function(url,json, callback) {
			console.log("xmlMappingUtil.postJson",url,json);
			$.ajax({
				  url: url,
				  method: "POST",
				  data: JSON.stringify(json),
				  processData : false,
			      contentType: "application/json; charset=utf-8",
				}).done(callback)
                .fail(function(jaxhr,textStatus,error) {
                	alert("Service error\n" + jaxhr.responseText);
                });
		},
		
		postAndExport : function(url,json) {
			console.log("xmlMappingUtil.postAndExport",url,json);
			$.ajax({
				  url: url,
				  method: "POST",
				  data: JSON.stringify(json),
				  processData : false,
			      contentType: "application/json; charset=utf-8",
			      error: function(XMLHttpRequest, textStatus, errorThrown) {
			    	  alert("Service error\n" + jaxhr.responseText);
			      },
			      success: function(message, textStatus, response) {
			         var header = response.getResponseHeader('Content-Disposition');
			         var fileName = header.split("=")[1];
			         var blob = new Blob([JSON.stringify(JSON.parse(message), undefined, 4)]);
			         var link = document.createElement('a');
			         link.href = window.URL.createObjectURL(blob);
			         link.download = fileName;
			         link.click();
			      }
				})
		},

		postAndImport : function(url, fileToImport, callback) {
			console.log("xmlMappingUtil.postAndImport",url);
			console.log("xmlMappingUtil.postAndImport",fileToImport);
			
			var formData = new FormData();
			formData.append("templateFile", fileToImport);
			
	          $.ajax({
	                url: url,
	                type: 'POST',
	                data: formData,
	                dataType: 'json',
	                mimeType: 'multipart/form-data',
	                contentType: false,
	                cache: false,
	                processData: false,	                
					}).done(callback)
			        .fail(function(jaxhr,textStatus,error) {
			        	alert("Service error\n" + jaxhr.responseText);
			        });
		},
		
		getUrlParams : function (url) {
		    let hashes = url.slice(url.indexOf('?') + 1).split('&')
		    let params = {} 
		    hashes.map(hash => {
		        let [key, val] = hash.split('=')
		        params[key] = decodeURIComponent(val)
		    })

		    return params
		},
		
		tr : function(values) {
			return "<tr>" + values.map(function(val) { return "<td>" + val + "</td>" }).join("\n") + "</tr>";
		},		
	}
})();