function onSubmitForm() {
    document.getElementById("successAlert").style.display = "block";
    setTimeout(function() {
        document.getElementById("successAlert").style.display = "none";
    }, 4000);
}