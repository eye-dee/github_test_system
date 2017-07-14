$("#getAll").click(() => {
    $.ajax({
        type: "GET",
        url: "/user",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (result) {
            let tasks = [];
            for (let i = 0; i < result.length; i++) {
                if (result[i].tasks.length === 0) {
                    tasks.push({
                            email: result[i].email,
                            git: result[i].githubNick,
                            time: null,
                            successful: null,
                            log: null
                        }
                    )
                }
                for (let j = 0; j < result[i].tasks.length; j++) {
                    let task = {
                        email: result[i].email,
                        git: result[i].githubNick,
                        time: result[i].tasks[j].startTime,
                        successful: result[i].tasks[j].successful,
                        log: result[i].tasks[j].log
                    };
                    tasks.push(task);
                }
            }
            setTable(tasks);
        },
        error: function (result) {
            this.message = result.toString();
            alert(result.toString());
        }
    })
});

var printIcon = function (cell, formatterParams) { //plain text value
    return "<i>penis</i>";
};

function setTable(data) {
    $("#example-table").tabulator({
        rowClick: function (e, id, dataa, row) {
            $("#log").text(JSON.stringify(dataa.log, null, ' '));
        },
        fitColumns: true,
        movableCols: true,
        movableRows: true,
        tooltips: true,
        columns: [
            {
                formatter: printIcon, width: 40, align: "center", cellClick: function (e, cell) {
                alert("Printing row data for")
            }
            },
            {title: "Email", field: "email", sorter: "string"},
            {title: "Git", field: "git", sorter: "string"},
            {title: "Start time", field: "time", width: 130, sorter: "date", align: "center"},
            {
                title: "Status", field: "successful", width: 80, align: "center",
                formatter: "tick", sorter: "boolean"
            }

        ]
    });
    $("#example-table").tabulator("setData", data);
}

$(window).resize(function () {
    $("#example-table").tabulator("redraw");
});