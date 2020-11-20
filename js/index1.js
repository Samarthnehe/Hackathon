var $messages = $('.messages-content');
var serverResponse = "wala";


var suggession;
//speech reco
try {
  var SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
  var recognition = new SpeechRecognition();
}
catch(e) {
  console.error(e);
  $('.no-browser-support').show();
}

$('#start-record-btn').on('click', function(e) {
  recognition.start();
});

recognition.onresult = (event) => {
  const speechToText = event.results[0][0].transcript;
 document.getElementById("MSG").value= speechToText;
  //console.log(speechToText)
  insertMessage()
}


function listendom(no){
  console.log(no)
  //console.log(document.getElementById(no))
document.getElementById("MSG").value= no.innerHTML;
  insertMessage();
}

$(window).load(function() {
  $messages.mCustomScrollbar();
  setTimeout(function() {
    serverMessage("Hello I am your Medi Bot");
    speechSynthesis.speak( new SpeechSynthesisUtterance('Hello I am your Medi Bot'))
  }, 300);

});

function updateScrollbar() {
  $messages.mCustomScrollbar("update").mCustomScrollbar('scrollTo', 'bottom', {
    scrollInertia: 10,
    timeout: 0
  });
}



function insertMessage() {
  msg = $('.message-input').val();
  if ($.trim(msg) == '') {
    return false;
  }
  $('<div class="message message-personal">' + msg + '</div>').appendTo($('.mCSB_container')).addClass('new');
  fetchmsg() 
  
  $('.message-input').val(null);
  updateScrollbar();

}

document.getElementById("mymsg").onsubmit = (e)=>{
  e.preventDefault() 
  insertMessage();
  // serverMessage("hello");
  // speechSynthesis.speak( new SpeechSynthesisUtterance("hello"))
}

function serverMessage(response2,flg=0) {

if(flg==1)
{
  response2=`<a>${response2}</a>`
}
  if ($('.message-input').val() != '') {
    return false;
  }
  $('<div class="message loading new"><figure class="avatar"><img src="css/bot.png" /></figure><span></span></div>').appendTo($('.mCSB_container'));
  updateScrollbar();
  

  setTimeout(function() {
    $('.message.loading').remove();
    $('<div class="message new"><figure class="avatar"><img src="css/bot.png" /></figure>' + response2 + '</div>').appendTo($('.mCSB_container')).addClass('new');
    updateScrollbar();
  }, 100 + (Math.random() * 20) * 100);

}


function fetchmsg(){

     var url = 'https://api-we-care.herokuapp.com/sendMsg';
      
      const data = new URLSearchParams();
      for (const pair of new FormData(document.getElementById("mymsg"))) {
          data.append(pair[0], pair[1]);
          console.log(pair)
      }
    
      console.log("abc",data)
        fetch(url, {
          method: 'POST',
          body:data
        }).then(res => res.json())
         .then(response => {
          console.log(response);
          if(response.Reply=="location")
          {
           locateLink()
          }
          else if(response.Reply=="weather")
          {
            weather()
          }
          // else if(response.Reply=="news")
          // {
          //   news()
          // }
          else{
            serverMessage(response.Reply);
            speechSynthesis.speak( new SpeechSynthesisUtterance(response.Reply))
          }
        
          
         })
          .catch(error => console.error('Error h:', error));

}

function locateLink()
{
   var lat;
    var long;
    var url="https://www.google.com/maps/search/hospitals/@"
        if(navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                lat  = position.coords.latitude;
                long = position.coords.longitude;
                url=url+lat.toString()+","+long.toString()+",12z?hl=en";
                console.log(url);
                serverMessage(url,1);
        speechSynthesis.speak( new SpeechSynthesisUtterance('Please use the link to view hospitals near you'))
            });
            window.open(url,'_blank');
        } else {
            alert("Sorry, your browser does not support HTML5 geolocation.");
        }
}
function weather()
{
   var lat;
    var long;
    var url="http://api.openweathermap.org/data/2.5/weather?"
        if(navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(async(position)=> {
                lat  = position.coords.latitude;
                long = position.coords.longitude;
                url=url+"lat="+lat.toString()+"&lon="+long.toString()+"&appid=848454c6ea5938654f6bd3b841b86136&units=metric";
      
                const api=await fetch(url)
                const data=await api.json()
                // console.log(data.main.temp);
                  serverMessage('The temperature is '+ data.main.temp + ' degree celcius. It is '+ data.weather[0].description+ ' outside',0);
                  speechSynthesis.speak( new SpeechSynthesisUtterance('The temperature is'+ data.main.temp + 'degree celcius. It is '+ data.weather[0].description+ ' outside'))
        
            });
        } else {
            alert("Sorry, your browser does not support HTML5 geolocation.");
        }
}

async function news()
{
  const api=await fetch('https://newsapi.org/v2/top-headlines?country=in&apiKey=c3acd4eec7fa42568e83db32cc09d155')
  const data=await api.json()
  for(var i=0;i<5;i++)
  {
    serverMessage(i+1+"."+data.articles[i].title,0);
    speechSynthesis.speak( new SpeechSynthesisUtterance(i+1+"."+data.articles[i].title))
  }
  // console.log(data.main.temp);
    
}

