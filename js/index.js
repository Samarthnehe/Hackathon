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