var feedback = function(res) {
    if (res.success === true) {
        var get_link = res.data.link.replace(/^http:\/\//i, 'https://');
        document.querySelector('.status').classList.add('bg-success');
        // document.querySelector('.status').innerHTML =
        //     'Image : ' + '<br><input class="image-url" value=\"' + get_link + '\"/>' + '<img class="img" alt="Imgur-Upload" src=\"' + get_link + '\"/>';
		// document.querySelector('.status').innerHTML =
        //     'Image : ' + '<br><input class="image-url" value=\"' + get_link + '\"/>';
		var url="https://api.ocr.space/parse/imageurl?apikey=de092d644088957&url="+get_link +"&OCREngine=2";
    	console.log(url);

    	var xhttp = new XMLHttpRequest();
  		xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		      var data=JSON.parse(this.responseText);
		      data=data.ParsedResults[0].ParsedText;
		      data=JSON.stringify(data)
		      // console.log(data);
		      data=data.split("\\n");
			  var j=[]
			  var show="<span><strong style='text-align:center;'>Prescription</strong> <br> ";
		      for(var i=14;i<data.length;i=i+3){
				  var k={"name":data[i],"frequency":data[i+1],"description":data[i+2]};
				  show=show+" Medicine: "+data[i]+" <br> "+"Frequency: "+data[i+1]+" <br> "+"Description: "+data[i+2]+" <br><br> ";
		      	j.push(k);
		      	// console.log(data[i]+" "+data[i+1]+" "+data[i+2]);
			  }
			  show=show+"</span>";
			  console.log(j);
			  document.getElementById("scanned").classList.add("dropzone1");
			  document.getElementById("scanned").innerHTML=show;
			  console.log(show);
		    }
	  	};
	  	xhttp.open("GET", url, true);
	  	xhttp.send();
   	}
};

new Imgur({
    clientid: '8740ef1c6f7e22b', //You can change this ClientID
    callback: feedback
});