/**
 * Created by Silence on 2017/8/8 0008.
 */
window.onload = function init() {
    console.log('ok');

    var eb = new EventBus("http://192.168.1.6:3000/eventbus");

    eb.onopen = function () {
        eb.registerHandler("news-feed", function (err, msg) {
            var message = msg.body;
            console.log(JSON.parse(message));

            var str = JSON.parse(message);
            var x=document.getElementById('myTable').insertRow(1);
            var x1=x.insertCell(0);
            var x2=x.insertCell(1);
            var x3=x.insertCell(2);
            var x4=x.insertCell(3);
            var x5=x.insertCell(4);
            var x6=x.insertCell(5);

            //  hdfs    路径格式转换
            //  hdfs://hadoop01:9000/user/root/images//222342484-29641-1471948966201.png
            //  user/root/images//222342484-29641-1471948966201.png
            //http://hadoop01:14000/webhdfs/v1/user/root/images//222342484-29641-1471948966201.png?user.name=hadoop&op=open
            x1.innerHTML=str.id;
            x2.innerHTML=str.name;

            var snapshoturl=get_hdfsurl(str.snapshoturl);
            var faceurl=get_hdfsurl(str.faceurl);
            x3.innerHTML="<img src='"+snapshoturl+"' width='100px'height='100px'>";
            x4.innerHTML="<img src='"+faceurl+"'width='100px'height='100px'>";
            console.log(snapshoturl);
            x5.innerHTML=str.similarity;
            x6.innerHTML=str.time;
            var str = "<code>" + msg.body + "</code><br>";
            $('#status').prepend(str);
        })
    }
}

function parseURL(url) {
    var a =  document.createElement('a');
    a.href = url;
    return {
        source: url,
        protocol: a.protocol.replace(':',''),
        host: a.hostname,
        port: a.port,
        query: a.search,
        params: (function(){
            var ret = {},
                seg = a.search.replace(/^\?/,'').split('&'),
                len = seg.length, i = 0, s;
            for (;i<len;i++) {
                if (!seg[i]) { continue; }
                s = seg[i].split('=');
                ret[s[0]] = s[1];
            }
            return ret;
        })(),
        file: (a.pathname.match(/\/([^\/?#]+)$/i) || [,''])[1],
        hash: a.hash.replace('#',''),
        path: a.pathname.replace(/^([^\/])/,'/$1'),
        relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [,''])[1],
        segments: a.pathname.replace(/^\//,'').split('/')
    };
}

function  get_hdfsurl(url) {
    var tmp="http"+url.substring(4);
    console.log(url)
    console.log("解析");
    var myurl=parseURL(tmp);
    var hdfs_url="http://192.168.1.6:50070/webhdfs/v1"+myurl.path+ "?user.name=hadoop&op=open";
    return hdfs_url;
}
