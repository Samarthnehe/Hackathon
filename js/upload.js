
var feedback = function(res) {
    if (res.success === true) {
        var get_link = res.data.link.replace(/^http:\/\//i, 'https://');
        document.querySelector('.status').classList.add('bg-success');
		var url="https://api.ocr.space/parse/imageurl?apikey=de092d644088957&url="+get_link +"&OCREngine=2";
    	console.log(url);
    	var xhttp = new XMLHttpRequest();
  		xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		      var data=JSON.parse(this.responseText);
		      data=data.ParsedResults[0].ParsedText;
		      data=JSON.stringify(data)
		      
			  data=data.split("\\n");
			  var j=[]
			  var today = new Date();
			  var dd = String(today.getDate()).padStart(2, '0');
			  var mm = String(today.getMonth() + 1).padStart(2, '0'); 
			  var yyyy = today.getFullYear();

			  today = yyyy+mm+dd;
			  var show="<span><strong style='text-align:center;'>Prescription</strong> <br> ";
		      for(var i=14;i<data.length;i=i+3){
				var k={"name":data[i],"frequency":data[i+1],"description":data[i+2]};
				show=show+" Medicine: "+data[i]+" <br> "+"Frequency: <br>";

				if(data[i+1][0]=='1'){
					show=show+"Morning:   10 AM  ";
					show=show+`<a class="calender" target="_blank" href="http://www.google.com/calendar/event?action=TEMPLATE&text=Take%20Medicine&dates=${today}T100000Z/${today}T110000&details=Take%20${data[i]}%20${data[i+2]}">Add to Calender</a><br><br>`;
				}
				if(data[i+1][2]=='1'){
					show=show+"Afternoon: 2 PM   ";
					show=show+`<a class="calender" target="_blank" href="http://www.google.com/calendar/event?action=TEMPLATE&text=Take%20Medicine&dates=${today}T140000Z/${today}T150000&details=Take%20${data[i]}%20${data[i+2]}">Add to Calender</a><br><br>`;
				}
				if(data[i+1][4]=='1'){
					show=show+"Evening:   6 PM   ";
					show=show+`<a class="calender" target="_blank" href="http://www.google.com/calendar/event?action=TEMPLATE&text=Take%20Medicine&dates=${today}T180000Z/${today}T190000&details=Take%20${data[i]}%20${data[i+2]}">Add to Calender</a><br><br>`;
				}
				if(data[i+1][6]=='1'){
					show=show+"Night:     9 PM   ";
					show=show+`<a class="calender" target="_blank" href="http://www.google.com/calendar/event?action=TEMPLATE&text=Take%20Medicine&dates=${today}T210000Z/${today}T220000&details=Take%20${data[i]}%20${data[i+2]}">Add to Calender</a><br><br>`;
				}

				show=show+"Description: "+data[i+2]+" <br><hr> ";
		      	j.push(k);
		      	
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
    clientid: '8740ef1c6f7e22b', 
    callback: feedback
});

