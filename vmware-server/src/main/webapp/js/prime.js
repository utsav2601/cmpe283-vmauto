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
    type : "get",
    cache: false,
    url : "/v2/stats/vm/laststate",
    success : vmLastState,
    error : showGenericError
  });
}


function vmLastState(text) {
  console.log(text);
  if(text.length > 0) {
    var lastState = "";
    $.each(text, function(index, value) {
      var lastStateTable = '<h3 class="sub-header">Last Known State for ' + value.vmName + '</h3><div class="table-responsive"><table class="table table-hover table-striped"><thead><tr><th>Key</th><th>Value</th></tr></thead><tbody>';
      lastStateTable += '<tr><td>Time Stamp</td><td>'+ new Date(value.timeStamp).toLocaleString() +'</td></tr>';
      lastStateTable += '<tr><td>Full Name</td><td>'+ value.guestFullName +'</td></tr>';
      lastStateTable += '<tr><td>IP Address</td><td>'+ value.guestIpAddress +'</td></tr>';
      lastStateTable += '<tr><td>System Uptime</td><td>'+ value.systemUpTime +'</td></tr>';
      lastStateTable += '<tr><td>System Power State</td><td>'+ value.powerState +'</td></tr>';
      lastStateTable += '<tr><td>Snap Shots</td><td>'+ value.supportsSnapShot +'</td></tr>';
      lastStateTable += '<tr><td>CPU Usage</td><td>'+ value.cpuUsage +'</td></tr>';
      lastStateTable += '<tr><td>Memory Usage</td><td>'+ value.guestMemoryUsage +'</td></tr>';
      lastStateTable += '<tr><td>Memory Allocated</td><td>'+ value.maxHostMemory +'</td></tr>';
      lastStateTable += '<tr><td>Storage Used</td><td>'+ value.storageUsed +'</td></tr>';
      lastStateTable += '<tr><td>Disk Read Average</td><td>'+ value.diskReadAverage +'</td></tr>';
      lastStateTable += '<tr><td>Disk Write Average</td><td>'+ value.diskWriteAverage +'</td></tr>';
      lastStateTable += '<tr><td>Disk Usage Average</td><td>'+ value.diskUsageAverage +'</td></tr>';
      lastStateTable += '<tr><td>Disk Total Latency</td><td>'+ value.diskTotalLantency +'</td></tr>';
      lastStateTable += '<tr><td>Net Usage Average</td><td>'+ value.netUsageAverage +'</td></tr>';
      lastStateTable += '<tr><td>Net Bytes Recieved</td><td>'+ value.netBytesRxAverage +'</td></tr>';
      lastStateTable += '<tr><td>Net Bytes Transmitted</td><td>'+ value.netBytesTxAverage +'</td></tr>';
      lastStateTable += '<tr><td>Datastore Read Average</td><td>'+ value.datastoreReadAverage +'</td></tr>';
      lastStateTable += '<tr><td>Datastore Write Average</td><td>'+ value.dataStoreWriteAverage +'</td></tr>';
      lastStateTable += '<tr><td>Process Count</td><td>'+ value.processCount +'</td></tr>';
      lastStateTable += '<tr><td>Thread Count</td><td>'+ value.threadCount +'</td></tr>';


      lastStateTable += '</tbody></table></div>';
      lastState += lastStateTable;
    });
    $("#lastKnownState").html(lastState);
    
    $.ajax({
      type: "get",
      cache: false,
      url : "/v2/stats/vm",
      success : showVMStats,
      error : showGenericError
    });
  }
  else {
    showGenericError();
  }
}



function showVMStats(text) {
  // Get list of unique vm
  var uniqueVMs = jQuery.parseJSON($.ajax({
    async: false,
    cache: false,
    url : "/v2/stats/vm/unique",
  }).responseText)

  console.log(uniqueVMs);
  console.log(text);

  google.load("visualization", "1", { packages:["corechart"], callback: function() {
    var cpuUsageData          = new google.visualization.DataTable();
    var memoryChartData       = new google.visualization.DataTable();
    var storageChartData      = new google.visualization.DataTable();
    var diskUsageChartData    = new google.visualization.DataTable();
    var diskReadChartData     = new google.visualization.DataTable();
    var diskWriteChartData    = new google.visualization.DataTable();
    var diskLatencyChartData  = new google.visualization.DataTable();
    var dsReadAvgChartData    = new google.visualization.DataTable();
    var dsWriteAvgChartData   = new google.visualization.DataTable();
    var netUsageChartData     = new google.visualization.DataTable();
    var netBytesRxChartData   = new google.visualization.DataTable();
    var netBytesTxChartData   = new google.visualization.DataTable();
    var processChartData      = new google.visualization.DataTable();
    var threadChartData       = new google.visualization.DataTable();

    // Configure Columns
    cpuUsageData.addColumn('datetime', 'Time');
    memoryChartData.addColumn('datetime', 'Time');
    storageChartData.addColumn('datetime', 'Time');
    diskUsageChartData.addColumn('datetime', 'Time');
    diskReadChartData.addColumn('datetime', 'Time');
    diskWriteChartData.addColumn('datetime', 'Time');
    diskLatencyChartData.addColumn('datetime', 'Time');
    dsReadAvgChartData.addColumn('datetime', 'Time');
    dsWriteAvgChartData.addColumn('datetime', 'Time');
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
      dsReadAvgChartData.addColumn('number', value);
      dsWriteAvgChartData.addColumn('number', value);
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
    dsReadAvgChartData.addRows(text.length);
    dsWriteAvgChartData.addRows(text.length);
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
      dsReadAvgChartData.setValue(index, 0, new Date(value.timeStamp));
      dsWriteAvgChartData.setValue(index, 0, new Date(value.timeStamp));
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
      dsReadAvgChartData.setValue(index, vmindex, value.datastoreReadAverage);
      dsWriteAvgChartData.setValue(index, vmindex, value.dataStoreWriteAverage);
      netUsageChartData.setValue(index, vmindex, value.netUsageAverage);
      netBytesRxChartData.setValue(index, vmindex, value.netBytesRxAverage);
      netBytesTxChartData.setValue(index, vmindex, value.netBytesTxAverage);
      processChartData.setValue(index, vmindex, value.processCount);
      threadChartData.setValue(index, vmindex, value.threadCount);
    });



    var cpuChartOptions = { title: 'CPU Usage MHz', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var memoryChartOptions = { title: 'Memory Usage MB', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var storageChartOptions = { title: 'Storage Usage MB', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var diskUsageChartOptions = { title: 'Disk Usage Usage MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var diskReadChartOptions = { title: 'Disk Average Read MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var diskWriteChartOptions = { title: 'Disk Average Write MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var diskLatencyChartOptions = { title: 'Disk Total Latency MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var dsReadAvgChartOptions = { title: 'Data Store Average Read MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var dsWriteAvgChartOptions = { title: 'Data Store Average Write MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
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
    var dsReadAvgChart = new google.visualization.LineChart(document.getElementById('dsReadAvgChart'));
    var dsWriteAvgChart = new google.visualization.LineChart(document.getElementById('dsWriteAvgChart'));
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
    dsReadAvgChart.draw(dsReadAvgChartData, dsReadAvgChartOptions);
    dsWriteAvgChart.draw(dsWriteAvgChartData, dsWriteAvgChartOptions);
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
    type : "get",
    cache: false,
    url : "/v2/stats/vhost/laststate",
    success : vHostLastState,
    error : showGenericError
  });
}


function vHostLastState(text) {
  console.log(text);
  if(text.length > 0) {
    var lastState = "";
    $.each(text, function(index, value) {
      var lastStateTable = '<h3 class="sub-header">Last Known State for ' + value.name + '</h3><div class="table-responsive"><table class="table table-hover table-striped"><thead><tr><th>Key</th><th>Value</th></tr></thead><tbody>';
      lastStateTable += '<tr><td>Time Stamp</td><td>'+ new Date(value.timeStamp).toLocaleString() +'</td></tr>';
      lastStateTable += '<tr><td>IP Address</td><td>'+ value.name +'</td></tr>';
      lastStateTable += '<tr><td>CPU Fairness</td><td>'+ value.cpuFairness +'</td></tr>';
      lastStateTable += '<tr><td>Memory Fairness</td><td>'+ value.memFairness +'</td></tr>';
      lastStateTable += '<tr><td>CPU Usage</td><td>'+ value.cpuUsage +'</td></tr>';
      lastStateTable += '<tr><td>CPU Freq</td><td>'+ value.cpuHz +'</td></tr>';
      lastStateTable += '<tr><td>Memory Usage</td><td>'+ value.memUsage +'</td></tr>';
      lastStateTable += '<tr><td>Disk Read Average</td><td>'+ value.diskReadAverage +'</td></tr>';
      lastStateTable += '<tr><td>Disk Write Average</td><td>'+ value.diskWriteAverage +'</td></tr>';
      lastStateTable += '<tr><td>Disk Usage Average</td><td>'+ value.diskUsageAverage +'</td></tr>';
      lastStateTable += '<tr><td>Disk Total Latency</td><td>'+ value.diskTotalLantency +'</td></tr>';
      lastStateTable += '<tr><td>Net Usage Average</td><td>'+ value.netUsageAverage +'</td></tr>';
      lastStateTable += '<tr><td>Net Bytes Recieved</td><td>'+ value.netBytesRxAverage +'</td></tr>';
      lastStateTable += '<tr><td>Net Bytes Transmitted</td><td>'+ value.netBytesTxAverage +'</td></tr>';
      lastStateTable += '<tr><td>Datastore Read Average</td><td>'+ value.datastoreReadAverage +'</td></tr>';
      lastStateTable += '<tr><td>Datastore Write Average</td><td>'+ value.dataStoreWriteAverage +'</td></tr>';

      lastStateTable += '</tbody></table></div>';
      lastState += lastStateTable;
    });
    $("#lastKnownState").html(lastState);

    $.ajax({
      type : "get",
      cache: false,
      url : "/v2/stats/vhost",
      success : showVHostStats,
      error : showGenericError
    });
  }
  else {
    showGenericError();
  }
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
    var cpuUsageData          = new google.visualization.DataTable();
    var memoryChartData       = new google.visualization.DataTable();
    var diskUsageChartData    = new google.visualization.DataTable();
    var diskReadChartData     = new google.visualization.DataTable();
    var diskWriteChartData    = new google.visualization.DataTable();
    var diskLatencyChartData  = new google.visualization.DataTable();
    var dsReadAvgChartData    = new google.visualization.DataTable();
    var dsWriteAvgChartData   = new google.visualization.DataTable();
    var netUsageChartData     = new google.visualization.DataTable();
    var netBytesRxChartData   = new google.visualization.DataTable();
    var netBytesTxChartData   = new google.visualization.DataTable();

    // Configure Columns
    cpuUsageData.addColumn('datetime', 'Time');
    memoryChartData.addColumn('datetime', 'Time');
    diskUsageChartData.addColumn('datetime', 'Time');
    diskReadChartData.addColumn('datetime', 'Time');
    diskWriteChartData.addColumn('datetime', 'Time');
    diskLatencyChartData.addColumn('datetime', 'Time');
    dsReadAvgChartData.addColumn('datetime', 'Time');
    dsWriteAvgChartData.addColumn('datetime', 'Time');
    netUsageChartData.addColumn('datetime', 'Time');
    netBytesRxChartData.addColumn('datetime', 'Time');
    netBytesTxChartData.addColumn('datetime', 'Time');

    $.each(uniqueVHosts, function(index, value) {
      cpuUsageData.addColumn('number', value);
      memoryChartData.addColumn('number', value);
      diskUsageChartData.addColumn('number', value);
      diskReadChartData.addColumn('number', value);
      diskWriteChartData.addColumn('number', value);
      diskLatencyChartData.addColumn('number', value);
      dsReadAvgChartData.addColumn('number', value);
      dsWriteAvgChartData.addColumn('number', value);
      netUsageChartData.addColumn('number', value);
      netBytesRxChartData.addColumn('number', value);
      netBytesTxChartData.addColumn('number', value);
    });
    cpuUsageData.addRows(text.length);
    memoryChartData.addRows(text.length);
    diskUsageChartData.addRows(text.length);
    diskReadChartData.addRows(text.length);
    diskWriteChartData.addRows(text.length);
    diskLatencyChartData.addRows(text.length);
    dsReadAvgChartData.addRows(text.length);
    dsWriteAvgChartData.addRows(text.length);
    netUsageChartData.addRows(text.length);
    netBytesRxChartData.addRows(text.length);
    netBytesTxChartData.addRows(text.length);


    
    // For each VM Record
    $.each(text, function(index, value) {
      var vmindex = uniqueVHosts.indexOf(value.name) + 1

      // Time Stamp rows
      cpuUsageData.setValue(index, 0, new Date(value.timeStamp));
      memoryChartData.setValue(index, 0, new Date(value.timeStamp));
      diskUsageChartData.setValue(index, 0, new Date(value.timeStamp));
      diskReadChartData.setValue(index, 0, new Date(value.timeStamp));
      diskWriteChartData.setValue(index, 0, new Date(value.timeStamp));
      diskLatencyChartData.setValue(index, 0, new Date(value.timeStamp));
      dsReadAvgChartData.setValue(index, 0, new Date(value.timeStamp));
      dsWriteAvgChartData.setValue(index, 0, new Date(value.timeStamp));
      netUsageChartData.setValue(index, 0, new Date(value.timeStamp));
      netBytesRxChartData.setValue(index, 0, new Date(value.timeStamp));
      netBytesTxChartData.setValue(index, 0, new Date(value.timeStamp));

      // Data records
      cpuUsageData.setValue(index, vmindex, value.cpuUsage);
      memoryChartData.setValue(index, vmindex, value.memUsage);
      diskUsageChartData.setValue(index, vmindex, value.diskUsageAverage);
      diskReadChartData.setValue(index, vmindex, value.diskReadAverage);
      diskWriteChartData.setValue(index, vmindex, value.diskWriteAverage);
      diskLatencyChartData.setValue(index, vmindex, value.diskTotalLantency);
      dsReadAvgChartData.setValue(index, vmindex, value.datastoreReadAverage);
      dsWriteAvgChartData.setValue(index, vmindex, value.dataStoreWriteAverage);
      netUsageChartData.setValue(index, vmindex, value.netUsageAverage);
      netBytesRxChartData.setValue(index, vmindex, value.netBytesRxAverage);
      netBytesTxChartData.setValue(index, vmindex, value.netBytesTxAverage);
    });



    var cpuChartOptions = { title: 'CPU Usage', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var memoryChartOptions = { title: 'Memory Usage MB', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var diskUsageChartOptions = { title: 'Disk Usage Usage MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var diskReadChartOptions = { title: 'Disk Average Read MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var diskWriteChartOptions = { title: 'Disk Average Write MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var diskLatencyChartOptions = { title: 'Disk Total Latency MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var dsReadAvgChartOptions = { title: 'Data Store Average Read MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var dsWriteAvgChartOptions = { title: 'Data Store Average Write MB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var netUsageChartOptions = { title: 'Net Usage Statistics KB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var netBytesRxChartOptions = { title: 'Net Bytes Recieved KB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };
    var netBytesTxChartOptions = { title: 'Net Bytes Transmitted KB/s', curveType: 'function', interpolateNulls: true, explorer: { actions: ['rightClickToReset'], maxZoomOut: 2, keepInBounds: true } };

    var cpuUsageChart = new google.visualization.LineChart(document.getElementById('cpuChart'));
    var memoryChart = new google.visualization.LineChart(document.getElementById('memoryChart'));
    var diskUsageChart = new google.visualization.LineChart(document.getElementById('diskUsageChart'));
    var diskReadChart = new google.visualization.LineChart(document.getElementById('diskReadChart'));
    var diskWriteChart = new google.visualization.LineChart(document.getElementById('diskWriteChart'));
    var diskLatencyChart = new google.visualization.LineChart(document.getElementById('diskLatencyChart'));
    var dsReadAvgChart = new google.visualization.LineChart(document.getElementById('dsReadAvgChart'));
    var dsWriteAvgChart = new google.visualization.LineChart(document.getElementById('dsWriteAvgChart'));
    var netUsageChart = new google.visualization.LineChart(document.getElementById('netUsageChart'));
    var netBytesRxChart = new google.visualization.LineChart(document.getElementById('netBytesRxChart'));
    var netBytesTxChart = new google.visualization.LineChart(document.getElementById('netBytesTxChart'));

    cpuUsageChart.draw(cpuUsageData, cpuChartOptions);
    memoryChart.draw(memoryChartData, memoryChartOptions);
    diskUsageChart.draw(diskUsageChartData, diskUsageChartOptions);
    diskReadChart.draw(diskReadChartData, diskReadChartOptions);
    diskWriteChart.draw(diskWriteChartData, diskWriteChartOptions);
    diskLatencyChart.draw(diskLatencyChartData, diskLatencyChartOptions);
    dsReadAvgChart.draw(dsReadAvgChartData, dsReadAvgChartOptions);
    dsWriteAvgChart.draw(dsWriteAvgChartData, dsWriteAvgChartOptions);
    netUsageChart.draw(netUsageChartData, netUsageChartOptions);
    netBytesRxChart.draw(netBytesRxChartData, netBytesRxChartOptions);
    netBytesTxChart.draw(netBytesTxChartData, netBytesTxChartOptions);


    closeAllText();
  }});
}



function retrieveLogFile() {
  showDataLoading();
  var vmname = $('#inputVMName').val();
  var filename = $('#inputFileName').val();
  if(vmname && filename) {
    $.ajax({
      type : "get",
      cache: false,
      url : "/v2/stats/log/single",
      data : {
        "vmname" : vmname,
        "filename" : filename
      },
      success : showLogData,
      error : showGenericError
    });
  }
  else {
    showGenericError();
  }
}

function showLogData(text) {
  console.log(text);
  if(text.length > 0) {
    var logTable = '<h3 class="sub-header">Log Files for "' +  text[0].vmName  + '", Files: "' + text[0].fileName + '"</h3><div class="table-responsive"><table class="table table-hover table-striped"><thead><tr><th>#</th><th>Time</th><th>File Content</th></tr></thead><tbody>';
    $.each(text, function(index, value) {
      logTable += '<tr><td>'+ new Date(value.timeStamp).toLocaleString() +'</td><td>'+value.fileContent+'</td></tr>';
    });
    
    logTable += '</tbody></table></div>';
    $("#fileData").html(logTable);
    closeAllText();
  }
  else {
    showGenericError();
  }
}