function getGithubInfo(user) {
    //1. Create an instance of XMLHttpRequest class and send a GET request using it.
    // The function should finally return the object(it now contains the response!)

    // Function searches for the User on GitHub's API (using REST API)
    // If no user is found, then the error function "noSuchUser()" is called.
    fetch("https://api.github.com/users/" + user)
        .then((result) => {
            if(result.status === 200){
                let found_user = result.json()
                found_user.then((user_info) => showUser(user_info))
            } else {
                noSuchUser()
            }
        })
}

function showUser(user_info) {
    $("#user_name").text("User's name: " + user_info.name)
    $("#user_picture").html("<img src='" + user_info.avatar_url + "' width='90%'>")
    $("#user_id").text("User ID: " + user_info.id)
    $("#user_url").html("<a href='"+ user_info.html_url + "/' > User's GitHub Page </a>")
    $("#user_bio").text(user_info.bio)
    $("#user_followers").text("User's total followers: " + user_info.followers)
    //2. set the contents of the h2 and the two div elements in the div '#profile' with the user content
}

function noSuchUser() {
    //3. set the elements such that a suitable message is displayed
    $("#user_name").text("User not found")
    $("#user_picture").html("<img src='' width='90%'>")
    $("#user_id").text("User not found")
    $("#user_url").html("<a href='' > User's GitHub Page not found </a>")

}

$(document).ready(function () {
    $(document).on('keypress', '#username', function (e) {
        //check if the enter(i.e return) key is pressed
        if (e.which === 13) {
            //get what the user enters
            let username = $(this).val();
            //reset the text typed in the input
            $(this).val("");
            //get the user's information and store the response
            let response = getGithubInfo(username);
            //if the response is successful show the user's details
            if (response.status === 200) {
                showUser(JSON.parse(response.responseText));
                //else display suitable message
            } else {
                noSuchUser(username);
            }
        }
    })
});
