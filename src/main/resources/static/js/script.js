function onSubmitForm() {
    var messageBox = document.getElementById('success-message');
    if (messageBox) {
        setTimeout(function() {
            messageBox.style.opacity = '0';
            setTimeout(function() {
                messageBox.style.display = 'none';
            }, 1000);
        }, 3000);
    }
}
function deleteRecord(button) {
    var result = confirm("Kaydı silmek istediğinize emin misiniz?");
    if (!result) {
        event.preventDefault();
    }
    return result;
}

window.onload = function() {
    var messageBox = document.getElementById('success-message');
    if (messageBox) {
        setTimeout(function() {
            messageBox.style.opacity = '0';
            setTimeout(function() {
                messageBox.style.display = 'none';
            }, 1000);
        }, 3000);
    }
};