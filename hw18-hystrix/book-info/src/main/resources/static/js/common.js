function handleError(jqXHR, bookId) {
    if (jqXHR !== undefined && jqXHR.status === 404) {
        bookNotFound(bookId)
    } else {
        error();
    }
}

function bookNotFound(id) {
    const mainDiv = $('#main-content');

    mainDiv.empty();

    mainDiv
        .append(
            $('<div class="container">')
                .append(
                    $('<div class="common-div">')
                        .append(
                            '<p>Book with id = ' + id + ' was not found</p>' +
                            '<a class="btn btn-primary" href="/">Back</a>'
                        )

                )
        )
}

function error() {
    const mainDiv = $('#main-content');

    mainDiv.empty();

    mainDiv
        .append(
            $('<div class="container common-div">')
                .append('<p>Some error occurred. Please, try once more later.</p>')
                .append('<a class="btn btn-primary" href="/">Back</a>')
        )
}

function sendFormData(form, uri, method, errorHandler) {
    let postData = {};

    $.each(form.serializeArray(),
        function(i, v) {
            postData[v.name] = v.value;
        });

    $.ajax({
        type: method,
        contentType: "application/json",
        mimeType: 'application/json',
        url: uri ,
        data: JSON.stringify(postData),
        success: function() {
            window.location.replace("/");
        },
        error: function() {
            errorHandler();
        }
    });
}

function postFormData(form, uri, errorHandler) {
    sendFormData(form, uri, "POST", errorHandler);
}

function putFormData(form, uri, errorHandler) {
    sendFormData(form, uri, "put", errorHandler);
}
