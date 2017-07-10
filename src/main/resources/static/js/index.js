$("#startButton").click(() => {
    const gitNick = $("#git").val();
    console.log("User " + gitNick + " started")
    $.ajax({
        type: "POST",
        url: "/user/register",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            githubNick: gitNick,
            email: $("#email").val()
        }),
        success: function (result) {
            console.log("User " + result.toString() + " successful");
        },
        error: function (result) {
            console.log("User " + result.toString() + " error");
        }
    });
});


$("#taskButton").click(() => {
    const gitNick = $("#git").val();
    $.ajax({
        type: "POST",
        url: "/task/new",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            githubNick: gitNick,
            email: $("#email").val()
        }),
        success: function (result) {
            console.log("Task " + result.toString() + " successful");
        },
        error: function (result) {
            console.log("Task " + result.toString() + " error");
        }
    })
});