function handleError(jqXHR, bookId) {
    if (jqXHR.status === 404) {
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
