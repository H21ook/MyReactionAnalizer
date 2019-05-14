var posts = [];

function showAndroidToast(toast) {
    AndroidInterface.showToast(toast);
}

function displayPosts() {
    for(var i = 0; i < posts.length; i++) {
        addPost(posts[i]);
    }
}

function addPost(post) {
    var node = document.createElement("div");
    var element = "";
    var video = {
        is: false,
        url: ""
    };
    element = 
    '<div class="card" id="card" onclick="myClick(event)">'+
        '<div class="card-body">';
    if(post.message) {
        element += '<p>' + post.message + '</p>';
    }
    if(post.full_picture) {
            element +='<img class="card-img-top" src="' + post.full_picture + '" alt="Card image cap">';
    }
    if(post.name) {
        element += '<h5>' + post.name + '</h5>';
    }
    if(post.description) {
        element += '<p>' + post.description + '</p>';
    }
    
    element += 
        '</div>' +
    '</div>';
    node.innerHTML = element;
    document.getElementById("container").appendChild(node);
}

function getPostData() {
    var data;
    try {
        data = AndroidInterface.getPostData();
        AndroidInterface.showToast(data.substring(0,  20));
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
                displayPosts();
            }
        } catch(err) {
            AndroidInterface.showToast(err.message);
        }
    }
}

//JQUERY
$(document).ready(function() {
$('#child1').hide();
});
$("#mybutton").click(function(){

$('#child').hide(1000, function(){
    $('#parent2').append($('#child1'));
    $('#child1').show(1000);
});

});
//JQUERY