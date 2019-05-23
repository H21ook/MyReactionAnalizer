var posts = [];
var charts = [];
var chartsObj = [];

function showAndroidToast(toast) {
    AndroidInterface.showToast(toast);
}

function displayPosts() {
    for(var i = 0; i < posts.length; i++) {
        addPost(posts[i],charts[i],i);
    }
}

function addPost(post,chartData, index) {
    var node = document.createElement("div");

    var element = "";
    
    element = 
    '<div class="card">'+
        '<div class="card-body">' +
            '<div id="post'+index+'">';
    if(post.message) {
        element += '<p>' + post.message + '</p>';
    }
    if(post.type) {
        if(post.type == 'video') {
            if(post.full_picture) {
                element +='<img class="card-img-top" src="' + post.full_picture + '" alt="Card image cap">';
            }
            // element +='<div class="fb-video" data-href="' + post.permalink_url + '" data-allowfullscreen="true" data-width="500"></div>';
        } else {
            if(post.full_picture) {
                element +='<img class="card-img-top" src="' + post.full_picture + '" alt="Card image cap">';
            }
        }
    }
    if(post.name) {
        element += '<h5 style="padding-top: 10px;">' + post.name + '</h5>';
    }
    if(post.description) {
        element += '<p style="padding-top: 0px;">' + post.description + '</p>';
    }
    
    element += 
            '</div>';

    element += 
            '<div id="chart' + index + '" class="chart-wrap"></div>';

    element += 
            '<div id="postControl">' +
                '<a id="flip-btn'+ index +'" class="ripple control-btn" onclick="postHide('+index+', false)">' +
                    '<i class="fas fa-exchange-alt"></i>' +
                '</a>' +
                '<a class="ripple control-btn" onclick="getChartImage('+index+')">' +
                    '<i class="fas fa-download"></i>' +
                '</a>' +
            '</div>';

    element += 
        '</div>' +
    '</div>';

    node.innerHTML = element;
    node.classList.add("post-wrap");
    document.getElementById("container").appendChild(node);
    
    //charts data
    var postChart = prepareChart(chartData, index);
    
    chartsObj.push(postChart);
    postHide(index, true);
}

function prepareChart(chartData, index) {
    var myChart = echarts.init(document.getElementById('chart' + index));
    var reactionIcons = {
        'Like': 'icons/like.png',
        'Sad': 'icons/sad.png',
        'Love': 'icons/love.png',
        'Angry': 'icons/angry.png',
        'Haha': 'icons/haha.png',
        'Wow': 'icons/wow.png'
    };
    var option = {
        xAxis: {
            type: 'category',
            data: ['Reactions'],
            axisLabel: {
                formatter: function (value) {
                    return '{' + value + '| }\n{value|' + value + '}';
                },
                margin: 0,
                rich: {
                    value: {
                        lineHeight: 0,
                        align: 'center'
                    }
                }
            }
        },
        tooltip: {},
        yAxis: {
            type: 'value'
        },
        color: ['#24A4EA', '#EC4964', '#FFE96F', '#662D10', '#24A4EA', '#ED6B0F'],
        series: [
            {
                name: 'Like',
                data: [{name: 'Like', value: parseInt(chartData.like)}],
                type: 'bar',
                label: {
                    normal: {
                        show:true,
                        color: '#000000',
                        position: 'top',
                        formatter: function (value) {
                            return '{' + value.name + '| }\n{value|' + value.value + '}';
                        },
                        rich: {
                            value: {
                                lineHeight: 20,
                                align: 'center',
                                color: '#000'
                            },
                            Like: {
                                height: 20,
                                backgroundColor: {
                                    image: reactionIcons.Like
                                }
                            }
                        }
                    }
                }
            },
            {
                name: 'Love',
                data: [{name: 'Love', value: parseInt(chartData.love)}],
                type: 'bar',
                label: {
                    normal: {
                        show:true,
                        color: '#000000',
                        position: 'top',
                    
                        formatter: function (value) {
                            return '{' + value.name + '| }\n{value|' + value.value + '}';
                        },
                        rich: {
                            value: {
                                lineHeight: 20,
                                align: 'center',
                                color: '#000'
                            },
                            Love: {
                                height: 20,
                                align: 'center',
                                backgroundColor: {
                                    image: reactionIcons.Love
                                }
                            }
                        }
                    }
                }
            },
            {
                name: 'Wow',
                data: [{name: 'Wow', value: parseInt(chartData.wow)}],
                type: 'bar',
                label: {
                    normal: {
                        show:true,
                        color: '#000000',
                        position: 'top',
                    
                        formatter: function (value) {
                            return '{' + value.name + '| }\n{value|' + value.value + '}';
                        },
                        rich: {
                            value: {
                                lineHeight: 20,
                                align: 'center',
                                color: '#000'
                            },
                            Wow: {
                                height: 20,
                                align: 'center',
                                backgroundColor: {
                                    image: reactionIcons.Wow
                                }
                            }
                        }
                    }
                }
            },
            {
                name: 'Haha',
                data: [{name: 'Haha', value: parseInt(chartData.haha)}],
                type: 'bar',
                label: {
                    normal: {
                        show:true,
                        color: '#000000',
                        position: 'top',
                    
                        formatter: function (value) {
                            return '{' + value.name + '| }\n{value|' + value.value + '}';
                        },
                        rich: {
                            value: {
                                lineHeight: 20,
                                align: 'center',
                                color: '#000'
                            },
                            Haha: {
                                height: 20,
                                align: 'center',
                                backgroundColor: {
                                    image: reactionIcons.Haha
                                }
                            }
                        }
                    }
                }
            },
            {
                name: 'Sad',
                data: [{name: 'Sad', value: parseInt(chartData.sad)}],
                type: 'bar',
                label: {
                    normal: {
                        show:true,
                        color: '#000000',
                        position: 'top',
                    
                        formatter: function (value) {
                            return '{' + value.name + '| }\n{value|' + value.value + '}';
                        },
                        rich: {
                            value: {
                                lineHeight: 20,
                                align: 'center',
                                color: '#000'
                            },
                            Sad: {
                                height: 20,
                                align: 'center',
                                backgroundColor: {
                                    image: reactionIcons.Sad
                                }
                            }
                        }
                    }
                }
            },
            {
                name: 'Angry',
                data: [{name: 'Angry', value: parseInt(chartData.angry)}],
                type: 'bar',
                label: {
                    normal: {
                        show:true,
                        color: '#000000',
                        position: 'top',
                    
                        formatter: function (value) {
                            return '{' + value.name + '| }\n{value|' + value.value + '}';
                        },
                        rich: {
                            value: {
                                lineHeight: 20,
                                align: 'center',
                                color: '#000'
                            },
                            Angry: {
                                height: 20,
                                align: 'center',
                                backgroundColor: {
                                    image: reactionIcons.Angry
                                }
                            }
                        }
                    }
                }
            }
        ],
        
    };
    myChart.setOption(option);
    return myChart;
}

function getPostData() {
    var data;
    try {
        data = AndroidInterface.getPostData();
    } catch(err) {
        AndroidInterface.showToast(err.message);
    }
    return JSON.parse(data);
}

function getChartData() {
    var data;
    try {
        data = AndroidInterface.getChartData();
    } catch(err) {
        AndroidInterface.showToast(err.message);
    }
    return JSON.parse(data);
}

function loadData(){
    var check = 0;
    while(check == 0) {
        try {
            check = AndroidInterface.checkLoadedData();
            if(check) {
                posts = getPostData();
                charts = getChartData();
                displayPosts();
            }
        } catch(err) {
            AndroidInterface.showToast(err.message);
        }
    }
}

//JQUERY
function postHide(i, start) {
    if(start) {
        if($('#chart'+i)[0].style.display == "none") {
            $('#post'+i).hide();
            $('#chart'+i).show();
        } else {
            $('#chart'+i).hide();
            $('#post'+i).show();
        }
    } else {
        if($('#chart'+i)[0].style.display == "none") {
            $('#post'+i).hide('clip', 200);
            setTimeout(function() {
                $('#chart'+i).show('slide', 100);
            }, 200);
        } else {
            $('#chart'+i).hide('clip', 200);
            setTimeout(function() {
                $('#post'+i).show('slide', 100);
            }, 200);
        }
    }
}

function getChartImage(i) {
    try {
        // AndroidInterface.showToast(chartsObj[0].getDataURL());
    } catch(err) {
        AndroidInterface.showToast(err.message);
    }
} 
// $(document).ready(function(){
//     $( "#demo" ).html( "<span class='red'>Hello <b>Again</b></span>" );
    // $("p").click(function(){
    //     alert("The paragraph was clicked.");
    // });
// });

    // $("#demo").click(function(){
    //     AndroidInterface.showToast("OK");
        // alert(JSON.stringify($(this)[0]));
        // document.getElementById("demo").innerHTML = JSON.stringify($(this)[0]);
        // document.getElementById("demo").innerHTML = JSON.stringify($(this)[0].children);
        // document.getElementById("demo").innerHTML = JSON.stringify($(this)[0].children[0]);
        // if($(this)[0].children[0].id.substring(0,4) == "card") {
        //     if($(this)[0].children[0].style.display == "none") {
        //         $("#"+$(this)[0].children[0].id).show(1000);
        //         $("#"+$(this)[0].children[1].id).hide(1000);
        //     } else {
        //         $("#"+$(this)[0].children[0].className).hide(1000);
        //         $("#"+$(this)[0].children[1].className).show(1000);
        //     }
        // }
    // });


// $(document).ready(function() {
// $('#child1').hide();
// });
// $("#mybutton").click(function(){

// $('#child').hide(1000, function(){
//     $('#parent2').append($('#child1'));
//     $('#child1').show(1000);
// });

// });
//JQUERY