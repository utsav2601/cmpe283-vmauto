$(function() {
  $( "#contentPage").load( "html/overview.html" );
  
  $("#overview").click(function() {
    setNavActive($(this), "html/overview.html");
  });

  $("#viewVMs").click(function() {
    setNavActive($(this), "html/vm.html"); 
  });

  $( "#viewVHosts" ).click(function() {
    setNavActive($(this), "html/vhost.html");
  });

  $( "#viewLogs" ).click(function() {
    setNavActive($(this), "html/logs.html");
  });
});


function setNavActive(element, url) {
  $("#navs li").each(function(i) {
    $(this).removeClass("active");
  });
  element.parent().addClass("active");
  $("#contentPage").load(url);
}