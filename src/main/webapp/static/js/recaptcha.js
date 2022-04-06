window.onload = function (){
    const form = document.getElementById("form");
    const error = document.getElementById("error");

    form.addEventListener("submit", function (event){
        event.preventDefault();
        const response = grecaptcha.getResponse();
        if (response){
            form.submit();
        } else {
            error.style.color = "#f2f2f2";
            error.style.fontSize = "small";
            error.innerHTML = "Please check";
        }
    });
}