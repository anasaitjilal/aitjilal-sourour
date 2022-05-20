$(document).ready(function(){


// # ===============================
// # = Nombre des produits
// # ===============================


// # ===============================
// # = Nombre des machines par marque
// # ===============================
    $.ajax({
        url : '/anass',
        contentType : "application/json",
        dataType : "json",
        data : '',
        type : 'GET',
        async : false,
        success : function(data, textStatus,
                           jqXHR) {
            console.log(data);
            var labels = new Array();
            var dt = new Array();
            Object.keys(data).forEach(key => {
                labels.push(data[key]); // returns the keys in an object
                dt.push(key);  // returns the appropriate value
            });

            var ctx = document.getElementById('myChart').getContext('2d');
            var myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        data: dt,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)',
                            'rgba(255, 159, 64, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)',
                            'rgba(255, 159, 64, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    title: {
                        display: true,
                        text: 'Nombre d examen par professeurs',
                        fontSize: 21,
                        padding: 20,
                        fontFamily: 'Calibri',
                    },
                    legend: {
                        display: false
                    },
                    type: 'line',
                    scales: {

                        yAxes: [{
                            ticks: {
                                beginAtZero: true
                            },
                            scaleLabel: {
                                display: true,
                                labelString: 'Nombre d examen '
                            }
                        }],
                        xAxes: [{
                            scaleLabel: {
                                display: true,
                                labelString: 'professeurs'
                            }
                        }],
                    }
                }
            });
        },
        error : function(jqXHR, textStatus,
                         errorThrown) {
            console.log(textStatus);
        }
    });
});
