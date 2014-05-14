var lastMessageId = -1;

function refresh() {
  $.ajax({
    dataType: "json",
    url: "api/v1/messages?after=" + lastMessageId + "&count=10",
    success: function(messages) {
      console.log("messages == " + JSON.stringify(messages));
      if(messages.length > 0) {
        lastMessageId = messages[messages.length - 1].id;
        $.each(messages, function(index, message) {
          $("#history").prepend("&gt; " + message.value + "\n");
        });
      }
      console.log("lastMessageId == " + lastMessageId);
      setTimeout(refresh, 1000);
    },
    error: function(xhr, status, error) {
      console.log(xhr);
      console.log(status);
      console.log(error);
    }
  });
}

$(document).ready(function() {
  $("#form").submit(function(event) {
    event.preventDefault();
    var value = $("#input").val();
    if(value != "") {
      $.post("api/v1/messages", value);
    }
    $("#input").val("");
  });
  refresh();
  console.log("OK!");
});


