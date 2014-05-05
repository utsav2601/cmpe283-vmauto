$(function() {
  $( "#contentPage").load( "html/overview.html" );
  
  $("#overview").click(function() {
    setNavActive($(this), "html/overview.html");
  });

  $("#viewVMs").click(function() {
    setNavActive($(this), "html/vm.html", loadVMStats); 
  });

  $( "#viewVHosts" ).click(function() {
    setNavActive($(this), "html/vhost.html", loadVHostStats);
  });

  $( "#viewLogs" ).click(function() {
    setNavActive($(this), "html/logs.html");
  });
});


function setNavActive(element, url, callbackFun) {
  $("#navs li").each(function(i) {
    $(this).removeClass("active");
  });
  element.parent().addClass("active");
  if(callbackFun) {
    $("#contentPage").load(url, callbackFun);
  }
  else {
    $("#contentPage").load(url);
  }
}


function showDataLoading() {
 $("#loadingDataErr").addClass("hidden");
 $("#loadingData").removeClass("hidden");
}

function showGenericError() {
  $("#loadingData").addClass("hidden");
  $("#loadingDataErr").removeClass("hidden");
}

function closeAllText() {
  $("#loadingData").addClass("hidden");
  $("#loadingDataErr").addClass("hidden");
}


function loadVMStats() {
  showDataLoading();
  $.ajax({
    type: "get",
    url : "/v2/stats/vm",
    success : showVMStats,
    error : showGenericError
  });
}



function showVMStats(text) {
  // Get list of unique vm
  var uniqueVMs = jQuery.parseJSON($.ajax({
    async: false,
    cache: true,
    url : "/v2/stats/vm/unique",
  }).responseText)

  console.log(uniqueVMs);
  console.log(text);

  google.load("visualization", "1", { packages:["corechart"], callback: function() {
    var cpuUsageData          =new google.visualization.DataTable();
    var memoryChartData       =new google.visualization.DataTable();
    var storageChartData      =new google.visualization.DataTable();
    var diskUsageChartData    =new google.visualization.DataTable();
    var diskReadChartData     =new google.visualization.DataTable();
    var diskWriteChartData    =new google.visualization.DataTable();
    var diskLatencyChartData  =new google.visualization.DataTable();
    var netUsageChartData     =new google.visualization.DataTable();
    var netBytesRxChartData   =new google.visualization.DataTable();
    var netBytesTxChartData   =new google.visualization.DataTable();
    var processChartData      =new google.visualization.DataTable();
    var threadChartData       =new google.visualization.DataTable();

    // Configure Columns
    cpuUsageData.addColumn('datetime', 'Time');
    memoryChartData.addColumn('datetime', 'Time');
    storageChartData.addColumn('datetime', 'Time');
    diskUsageChartData.addColumn('datetime', 'Time');
    diskReadChartData.addColumn('datetime', 'Time');
    diskWriteChartData.addColumn('datetime', 'Time');
    diskLatencyChartData.addColumn('datetime', 'Time');
    netUsageChartData.addColumn('datetime', 'Time');
    netBytesRxChartData.addColumn('datetime', 'Time');
    netBytesTxChartData.addColumn('datetime', 'Time');
    processChartData.addColumn('datetime', 'Time');
    threadChartData.addColumn('datetime', 'Time');

    $.each(uniqueVMs, function(index, value) {
      cpuUsageData.addColumn('number', value);
      memoryChartData.addColumn('number', value);
      storageChartData.addColumn('number', value);
      diskUsageChartData.addColumn('number', value);
      diskReadChartData.addColumn('number', value);
      diskWriteChartData.addColumn('number', value);
      diskLatencyChartData.addColumn('number', value);
      netUsageChartData.addColumn('number', value);
      netBytesRxChartData.addColumn('number', value);
      netBytesTxChartData.addColumn('number', value);
      processChartData.addColumn('number', value);
      threadChartData.addColumn('number', value);
    });
    cpuUsageData.addRows(text.length);
    memoryChartData.addRows(text.length);
    storageChartData.addRows(text.length);
    diskUsageChartData.addRows(text.length);
    diskReadChartData.addRows(text.length);
    diskWriteChartData.addRows(text.length);
    diskLatencyChartData.addRows(text.length);
    netUsageChartData.addRows(text.length);
    netBytesRxChartData.addRows(text.length);
    netBytesTxChartData.addRows(text.length);
    processChartData.addRows(text.length);
    threadChartData.addRows(text.length);


    
    // For each VM Record
    $.each(text, function(index, value) {
      var vmindex = uniqueVMs.indexOf(value.vmName) + 1;

      // Time Stamp rows
      cpuUsageData.setValue(index, 0, new Date(value.timeStamp));
      memoryChartData.setValue(index, 0, new Date(value.timeStamp));
      storageChartData.setValue(index, 0, new Date(value.timeStamp));
      diskUsageChartData.setValue(index, 0, new Date(value.timeStamp));
      diskReadChartData.setValue(index, 0, new Date(value.timeStamp));
      diskWriteChartData.setValue(index, 0, new Date(value.timeStamp));
      diskLatencyChartData.setValue(index, 0, new Date(value.timeStamp));
      netUsageChartData.setValue(index, 0, new Date(value.timeStamp));
      netBytesRxChartData.setValue(index, 0, new Date(value.timeStamp));
      netBytesTxChartData.setValue(index, 0, new Date(value.timeStamp));
      processChartData.setValue(index, 0, new Date(value.timeStamp));
      threadChartData.setValue(index, 0, new Date(value.timeStamp));

      // Data records
      cpuUsageData.setValue(index, vmindex, value.cpuUsage);
      memoryChartData.setValue(index, vmindex, value.guestMemoryUsage);
      storageChartData.setValue(index, vmindex, parseInt(value.storageUsed));
      diskUsageChartData.setValue(index, vmindex, value.diskUsageAverage);
      diskReadChartData.setValue(index, vmindex, value.diskReadAverage);
      diskWriteChartData.setValue(index, vmindex, value.diskWriteAverage);
      diskLatencyChartData.setValue(index, vmindex, value.diskTotalLantency);
      netUsageChartData.setValue(index, vmindex, value.netUsageAverage);
      netBytesRxChartData.setValue(index, vmindex, value.netBytesRxAverage);
      netBytesTxChartData.setValue(index, vmindex, value.netBytesTxAverage);
      processChartData.setValue(index, vmindex, value.processCount);
      threadChartData.setValue(index, vmindex, value.threadCount);
    });



    var cpuChartOptions = { title: 'CPU Usage MHz', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var memoryChartOptions = { title: 'Memory Usage MB', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var storageChartOptions = { title: 'Stroage Usage MB', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var diskUsageChartOptions = { title: 'Disk Usage Usage MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var diskReadChartOptions = { title: 'Disk Average Read MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var diskWriteChartOptions = { title: 'Disk Average Write MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var diskLatencyChartOptions = { title: 'Disk Total Latency MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var netUsageChartOptions = { title: 'Net Usage Statistics KB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var netBytesRxChartOptions = { title: 'Net Bytes Recieved KB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var netBytesTxChartOptions = { title: 'Net Bytes Transmitted KB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var processChartOptions = { title: 'VM\'s Process Count', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var threadChartOptions = { title: 'VM\'s Thread Count', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };


    var cpuUsageChart = new google.visualization.LineChart(document.getElementById('cpuChart'));
    var memoryChart = new google.visualization.LineChart(document.getElementById('memoryChart'));
    var storageChart = new google.visualization.LineChart(document.getElementById('storageChart'));
    var diskUsageChart = new google.visualization.LineChart(document.getElementById('diskUsageChart'));
    var diskReadChart = new google.visualization.LineChart(document.getElementById('diskReadChart'));
    var diskWriteChart = new google.visualization.LineChart(document.getElementById('diskWriteChart'));
    var diskLatencyChart = new google.visualization.LineChart(document.getElementById('diskLatencyChart'));
    var netUsageChart = new google.visualization.LineChart(document.getElementById('netUsageChart'));
    var netBytesRxChart = new google.visualization.LineChart(document.getElementById('netBytesRxChart'));
    var netBytesTxChart = new google.visualization.LineChart(document.getElementById('netBytesTxChart'));
    var processChart = new google.visualization.LineChart(document.getElementById('processChart'));
    var threadChart = new google.visualization.LineChart(document.getElementById('threadChart'));


    cpuUsageChart.draw(cpuUsageData, cpuChartOptions);
    memoryChart.draw(memoryChartData, memoryChartOptions);
    storageChart.draw(storageChartData, storageChartOptions);
    diskUsageChart.draw(diskUsageChartData, diskUsageChartOptions);
    diskReadChart.draw(diskReadChartData, diskReadChartOptions);
    diskWriteChart.draw(diskWriteChartData, diskWriteChartOptions);
    diskLatencyChart.draw(diskLatencyChartData, diskLatencyChartOptions);
    netUsageChart.draw(netUsageChartData, netUsageChartOptions);
    netBytesRxChart.draw(netBytesRxChartData, netBytesRxChartOptions);
    netBytesTxChart.draw(netBytesTxChartData, netBytesTxChartOptions);
    processChart.draw(processChartData, processChartOptions);
    threadChart.draw(threadChartData, threadChartOptions);


    closeAllText();
  }});
}




function loadVHostStats() {
  showDataLoading();
  $.ajax({
    type: "get",
    url : "/v2/stats/vm",
    success : showVHostStats,
    error : showGenericError
  });
}


function showVHostStats(text) {
    // Get list of unique vm
  var uniqueVHosts = jQuery.parseJSON($.ajax({
    async: false,
    cache: true,
    url : "/v2/stats/vhost/unique",
  }).responseText)

  console.log(uniqueVHosts);
  console.log(text);


  google.load("visualization", "1", { packages:["corechart"], callback: function() {

    closeAllText();
  }});
}

