var _info = navigator.userAgent;
var ie = (_info.indexOf("MSIE") > 0);
var win = (_info.indexOf("Win") > 0);
var nn6 = (document.getElementById && !document.all);
var ie5 = (document.all && document.getElementById);
//outPrint = window.open("","output")


function loadPage(id,url) {
    var content_width = 976;

    if (ie5 || nn6) {
        /*
        for(i=0;i<document.forms.length;++i) {
            for (j=0;j<document.forms[i].elements.length;++j)  {
                if (nestref!='null' || document.forms[i].elements[j].name==nestref) {
                    document.getElementById('contentFRM').src = url+'?'+nestref+'='+document.forms[i].elements[j].value;
                } else {
                    document.getElementById('contentFRM').src = url;
                }
            }
        }
        */
        document.getElementById('contentFRM').src=url;

        if (document.forms.length!=null) {
            for(i=0;i<document.forms.length;++i) {
                for (j=0;j<document.forms[i].elements.length;++j)  {
                        if (document.forms[i].elements[j].name !=null && j==0 || i == 0) {
                                    document.getElementById('contentFRM').src=url+'?'+document.forms[i].elements[0].name+'='+document.forms[0].elements[0].value;
                        }
                        if (i>0 || j>0) {
                             if (document.forms[i].elements[j].name !=null && document.forms[i].elements[j].value !=null)
                                 +"&"+document.forms[i].elements[j].name+'='+document.forms[i].elements[j].value;
                        }
                    }
               }
         }
        //alert("forms: " + document.forms.length + " url: " + url + " src: " +  document.getElementById('contentFRM').src);
        //alert(document.getElementById('contentFRM').src)
        //alert(window.location.href.indexOf('http'))
        //alert(window.location.href)
        //alert(document.getElementById('contentFRM').src = eval('contentFRM' + "_on.src");)
        //alert("ie5 nn6 " + url+'?encoderIP='+ document.getElementById('encoderIP').valueOf());
        //alert("ie5 nn6 " + url+'?encoderIP='+ nestref);
    } else alert("Browser not supported");
}

function showPage(id) {
    if (nn6 || ie5) { 
        //var namevalue= document.getElementById('theBody').innerHTML;
        document.getElementById(id).innerHTML = window.frames['contentFRM'].document.getElementById('theBody').innerHTML;
        //var namevalue= document.getElementById(id).innerHTML;
        //get value to post from a form field
        //var searchvalue= window.frames['contentFRM'].document.getElementById('searchform').query.value;
        //var searchvalue= document.getElementsByTagName("FORM") [0].q.value;
        //var poststr= "name=" + encodeURI(namevalue) + "&q=" + encodeURI(searchvalue);
        //alert(poststr);
    } else alert("Browser not supported");
}
/*
function toggleBg(elementShow, elementHide) {
    showContent(elementShow);
    hideContent(elementHide);
}

function showContent(elementId) {
    if (elementId.length < 1) {
        return;
    } else {
       alert("debug " + elementId);
       if (nn6 || ie5) {
            document.getElementById(elementId).style.display = "inline";
        }
    }
}
*/

function showContent(elementId) {}

function hideContent(elementId) {
    if (elementId.length < 1) {
        return;
    } else {
        //alert("debug " + elmentId);
        if (nn6 || ie5) {
            document.getElementById(elementId).style.display = "none";
        }
    }
}

function reverseContentDisplay(elementId) {
    if (elementId.length < 1) {
        return;
    } else {

        //debug ("elmentId: "+ elementId + " " +
        //	"display: " + document.getElementById(elementId).style.display);

        //printDOMTree( document.getElementById(elementId));

        if (nn6 || ie5) {
            if (document.getElementById(elementId).style.display == "none") {
                document.getElementById(elementId).style.display = "inline";
            } else {
                document.getElementById(elementId).style.display = "none";
            }
        }
    }
}

function getFormInfo(form) {
    var info;
    //alert(form)
    //countTags(document.forms);
    //debug(print);
    var print = printDOMTree(document.getElementById('encoder'));
    debug(print);
    //debug(document.body)

    //traverse()

    // Get a reference using the forms collection
    var f = document.forms[form];
    info = "form elements: " + f.elements + "\n"
            + "form length: " + f.length + "\n"
            + "form name: " + f.elements + "\n"
            + "form acceptCharset: " + f.acceptCharset + "\n"
            + "form action: " + f.action + "\n"
            + "form enctype: " + f.enctype + "\n"
            + "form encoding: " + f.encoding + "\n"
            + "form method: " + f.method + "\n"
            + "form target: " + f.target;
    //for(j=0;j< document.forms[form].elements.length;++j) {
    //document.forms[form].elements[j].value = info;
    //	describeElement( parent.document.forms[form].elements[j])
    // }
    //outPrint.document.write("<H4> Form info </H4>")
    // outPrint.document.writeln(info)
    //debug(info)
    //outPrint.document.write("<BR/>")
}
