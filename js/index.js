function register()
{
    var data=
    {
	"email":document.getElementsByClassName('email')[0].value,
    "password":document.getElementsByClassName('pass')[0].value,
    "name":document.getElementsByClassName('name')[0].value
    }
    console.log(data)
    var xh = new XMLHttpRequest();
    xh.open("POST", "https://api-we-care.herokuapp.com/user/create", true)
    xh.setRequestHeader('Content-Type', 'application/json')
    xh.send(JSON.stringify(data))
    xh.onload=function(){
        if(this.status==201)
        {
            alert('registered successfully! Login to continue')
            window.location.replace('login.html')
        }
        else{
            alert('Failed! Try again')
            window.location.replace('signup.html')
        }
}
}

function login()
{
    var data=
    {
	"email":document.getElementsByClassName('email')[0].value,
	"password":document.getElementsByClassName('pass')[0].value
    }

    var xh = new XMLHttpRequest();
    xh.open("POST", "https://api-we-care.herokuapp.com/user/login", true)
    xh.setRequestHeader('Content-Type', 'application/json')
    xh.send(JSON.stringify(data))
    xh.onload=function(){
        if(this.status==200)
        {
            var data = JSON.parse(this.responseText)
            localStorage.setItem("JWT_Token", "JWT " + data.token)
            localStorage.setItem("user", JSON.stringify(data.user))
            window.location.replace('dashboard.html')
        }
        else{
            alert('Invalid login credentials')
            window.location.replace('login.html')
        }
}
}

function dashboard()
{
    document.getElementById('name').innerHTML=JSON.parse(localStorage.getItem("user")).name
    document.getElementById('email').innerHTML=JSON.parse(localStorage.getItem("user")).email
}

function suggestion()
{
    var xh = new XMLHttpRequest();
    var id=JSON.parse(localStorage.getItem("user"))._id
    xh.open("GET", `https://api-we-care.herokuapp.com/get/disease/${id}`, true)
    xh.setRequestHeader('Content-Type', 'application/json')
    xh.send()
    xh.onload=function(){
        if(this.status==200)
        {
            var res = JSON.parse(this.responseText)
            console.log(res.data)
            for(var i=0;i<res.data.length;i++)
            {
                $('#disp').append(`<div class="col-6 card add" data-aos="fade-left" style="border: 3px dashed black;">
              
                <div style="margin: 1em 2em;" >
                <div style="display: flex;align-items: center;">
                <button type="button" class="btn btn-primary" data-toggle="collapse" data-target="#demo${i+1}">+</button><h4 style="margin-left:5px;">${res.data[i].name}</h4>
                </div>
                
                <div id=demo${i+1} class="collapse">
                    <div style="margin: 2em 1em;">
                        <h5 style="color: green;"><b>DO'S</b></h5>
                        <div >
                        <ul id=do${i+1}>
                            
                        </ul>
                        </div>
                    </div>
                    <div style="margin: 2em 1em;">
                        <h5 style="color: red;"><b>DONT'S</b></h5>
                        <div >
                        <ul id=dont${i+1}>
                            
                        </ul>
                        </div>
                    </div>
                    <div style="margin: 2em 1em;">
                        <h5 style="color: blue;"><b>PREFERRED DIET</b></h5>
                        <div >
                        <ul id=diet${i+1}>
                            
                        </ul>
                        </div>
                    </div>
                </div>
                </div>
            
            </div>`)
            for(var j=0;j<res.data[i].cure.length;j++)
            {
                $(`#do${i+1}`).append(`
                    <li>${res.data[i].cure[j].line}</li>`)
            }
            for(var j=0;j<res.data[i].donts.length;j++)
            {
                $(`#dont${i+1}`).append(`
                    <li>${res.data[i].donts[j].line}</li>`)
            }
            for(var j=0;j<res.data[i].diet.length;j++)
            {
                $(`#diet${i+1}`).append(`
                    <li>${res.data[i].diet[j].item}</li>`)
            }

            }
        }
        else{
            
        }
}
}

function logout()
{
    localStorage.removeItem('user')
    localStorage.removeItem('JWT_Token')
    window.location.replace('login.html')
}

function check()
{
    var jwt=localStorage.getItem('JWT_Token')
    if(!jwt)
    {
        
        window.location.replace('login.html')

    }
}