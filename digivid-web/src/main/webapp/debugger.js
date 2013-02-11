function getFormElementInfo(formE) {
    var feInfo;
    var fe = formE;
    //HTMLInputElement : HTMLElement
    feInfo = "element default value: " + e.defaultValue + "\n"
            + "element defaultChecked: " + e.defaultChecked + "\n"
    //       	e.form;
            + "element accept: " + fe.accept + "\n"
            + "element accessKey: " + fe.accessKey + "\n"
            + "element align: " + fe.align + "\n"
            + "element alt: " + fe.alt + "\n"
            + "element checked: " + fe.checked + "\n"
            + "element disabled: " + fe.disabled + "\n"
            + "element maxLength: " + fe.maxLength + "\n"
            + "element name: " + fe.name + "\n"
            + "element readOnly: " + fe.readOnly + "\n"
            + "element size: " + fe.size + "\n"
            + "element src: " + fe.src + "\n"
            + "element tabIndex: " + fe.tabIndex + "\n"
            + "element type: " + fe.type + "\n"
            + "element useMap: " + fe.useMap + "\n";
    + "element value: " + fe.value;
    //alert(eInfo);
    //outPrint.document.write("<H4> Form Element info </H4>")
    //outPrint.document.writeln(feInfo)
    //outPrint.document.write("<BR/>")
}

function displayInfo(id, select) {

    alert(getFormInfo(select));
    var id = id;
    if (test = parent.contentFRM.document.getElementById("paragraph")) {
        parent.contentFRM.document.body.removeChild(test);
    }
    var info = document.createElement("p");


    info.setAttribute("id", "paragraph");
    //alert(id + " " + select);

    switch (select) {
        case "attributes":
            var text = "You have getAttribute and setAttribute. getAttribute takes one argument, setAttribute takes two.";
            break;
        case "tree":
            var text = "There are two functions: getElementsByTagName, which returns an array, and getElementById, which returns a single element based on the id attribute.";
            break;
        case "god":
            var text = "There are many functions. createElement, appendChild, insertBefore, createTextNode, and removeChild.";
            break;
    }

    //info.appendChild(text);
    if (ie4) {
        document.all[id].innerHTML = parent.contentFRM.document.body.appendChild(info);
    }
    else if (nn6 || ie5) {
        document.getElementById(id).innerHTML = window.frames['contentFRM'].document.getElementById('theBody').appendChild(info);
    }
    info.innerHTML = text;
    //info.innerHTML = "testing";
    //info.appendChild(text);
}



function land(ref, target) {

    lowtarget = target.toLowerCase();

    if (lowtarget == "_self") {
        window.location = ref;
    } else if (lowtarget == "_top") {
        top.location = ref;
    } else if (lowtarget == "_blank") {
        window.open(ref);
    } else if (lowtarget == "_parent") {
        parent.location = ref;
    } else {
        parent.frames[target].location = ref;
    }
}

function jumpSelect(menu) {
    ref = menu.choice.options[menu.choice.selectedIndex].value;
    splitc = ref.lastIndexOf("*");
    target = "";
    if (splitc != -1) {
        loc = ref.substring(0, splitc);
        target = ref.substring(splitc + 1, 1000);
    } else {
        loc = ref;
        target = "_self";
    }
    ;
    if (ref != "") {
        land(loc, target);
    }
}

function jumpText(menu) {
    ref = document.getElementsByTagName("FORM") [0].getAttributeNode("action").value;
    //target=document.getElementsByTagName("FORM") [0].getAttributeNode("target").value;
    //if (target == "")
    target = "_blank";
    q = menu.q.value;
    loc = ref + "?q=" + q;
    if (loc != "") {
        land(loc, target);
    }
}

function debug(msg) {
    // If we haven't already created a box within which to display
    // our debugging messages, then do so now. Note that to avoid
    // using another global variable, we store the box node as
    // a proprty of this function.
    if (!debug.box) {
        // Create a new <div> element
        debug.box = document.createElement("div");
        // Specify what it looks like using CSS style attributes
        debug.box.setAttribute("style",
                "background-color: white; " +
                "font-family: monospace; " +
                "border: solid red 3px; " +
                "position:absolute; " +
                "width:950px; " +
                "height:400px; " +
                "z-index:100; " +
                "left:10px; " +
                "top:576px; " +
                "padding: 10px;");

        // Append our new <div> element to the end of the document
        document.body.appendChild(debug.box);

        // Now add a title to our <div>. Note that the innerHTML property is
        // used to parse a fragment of HTML and insert it into the document.
        // innerHTML is not part of the W3C DOM standard, but it is supported
        // by Netscape 6 and Internet Explorer 4 and later. We can avoid
        // the use of innerHTML by explicitly creating the <h1> element,
        // setting its style attribute, adding a Text node to it, and
        // inserting it into the document, but this is a nice shortcut.
        debug.box.innerHTML = "<h1 style='text-align:center'>Debugging Output</h2>";
    }

    // When we get here, debug.box refers to a <div> element into which
    // we can insert our debugging message.
    // First create a <p> node to hold the message.
    var p = document.createElement("p");
    // Now create a text node containing the message, and add it to the <p>
    p.appendChild(document.createTextNode(msg));
    // And append the <p> node to the <div> that holds the debugging output
    debug.box.appendChild(p);
}


function traverseDomTree1() {
    var body_element = document.getElementsByTagName("body").item(0);
    traverseDomTree1_recurse(body_element, 0);
    //alert("The end");
}

function traverseDomTree1_recurse(curr_element, level) {
    var i;
    if (curr_element.childNodes.length <= 0) {
        // This is a leaf node.
        if (curr_element.nodeName == "#text") {
            // This is a text leaf node,
            // with the following text.
            var node_text = curr_element.data;
        }
    } else {
        // Expand each of the children of this node.
        for (i = 0; curr_element.childNodes.item(i); i++) {
            traverseDomTree1_recurse(curr_element.childNodes.item(i), level + 1);
        }
    }
}


////////////////////////////////////////////
// This function traverses the DOM tree of an element and prints the tree.
// This function called recursively until the DOM tree is fully traversed.
//
// Parameters:
// - targetDocument is where the tree will be printed into
// - currentElement is the element that we want to print
// - depth is the depth of the current element
//   (it should be 1 for the initial element)
////////////////////////////////////////////


function traverseDomTree2(targetDocument, currentElement, depth) {
    if (currentElement)
    {
        var j;
        var tagName = currentElement.tagName;
        // Prints the node tagName, such as <A>, <IMG>, etc
        if (tagName)
            targetDocument.writeln("&lt;" + currentElement.tagName + "&gt;");
        else
            targetDocument.writeln("[unknown tag]");

        // Traverse the tree
        var i = 0;
        var currentElementChild = currentElement.childNodes[i];
        while (currentElementChild)
        {
            // Formatting code (indent the tree so it looks nice on the screen)
            targetDocument.write("<BR>\n");
            for (j = 0; j < depth; j++)
            {
                // &#166 is just a vertical line
                targetDocument.write("&nbsp;&nbsp;&#166");
            }
            targetDocument.writeln("<BR>");
            for (j = 0; j < depth; j++)
            {
                targetDocument.write("&nbsp;&nbsp;&#166");
            }
            if (tagName)
                targetDocument.write("--");

            // Recursively traverse the tree structure of the child node
            traverseDomTree2(targetDocument, currentElementChild, depth + 1);
            i++;
            currentElementChild = currentElement.childNodes[i];
        }
        // The remaining code is mostly for formatting the tree
        targetDocument.writeln("<BR>");
        for (j = 0; j < depth - 1; j++)
        {
            targetDocument.write("&nbsp;&nbsp;&#166");
        }
        targetDocument.writeln("&nbsp;&nbsp;");
        if (tagName)
            targetDocument.writeln("&lt;/" + tagName + "&gt;");
    }
}

////////////////////////////////////////////
// This function accepts a DOM element as parameter and prints
// out the DOM tree structure of the element.
////////////////////////////////////////////
function printDOMTree(domElement) {

    var outputWindow = window.open("", "output");
    // Use destination window to print the tree.  If destinationWIndow is
    //   not specified, create a new window and print the tree into that window
    //  var outputWindow=destinationWindow;
    //  if (!outputWindow)
    //    outputWindow=window.open();

    // make a valid html page
    outputWindow.document.open("text/html", "replace");
    outputWindow.document.write("<HTML><HEAD><TITLE>DOM</TITLE></HEAD><BODY>\n");
    outputWindow.document.write("<CODE>\n");
    traverseDomTree2(outputWindow.document, domElement, 2);
    outputWindow.document.write("</CODE>\n");
    outputWindow.document.write("</BODY></HTML>\n");

    // Here we must close the document object, otherwise Mozilla browsers
    // might keep showing "loading in progress" state.
    outputWindow.document.close();
}

function change(id) {
    var p = document.getElementById(id).innerHTML;

    p.lastChild.nodeValue = window.frames['contentFRM'].document.getElementById('theBody').innerHTML;
}
//	function countCharacters(n) {                // n is a Node
//    		if (n.nodeType == 3 /*Node.TEXT_NODE*/)  // Check if n is a Text object
//        		return n.length;                     // If so, return its length
//    		// Otherwise, n may have children whose characters we need to count
//    		var numchars = 0;  // Used to hold total characters of the children
//    		// Instead of using the childNodes property, this loop examines the
//    		// children of n using the firstChild and nextSibling properties.
//    		for(var m = n.firstChild; m != null; m = m.nextSibling) {
//        		numchars += countCharacters(m);  // Add up total characters found
//    		}
//    		//return numchars;                     // Return total characters
//		 outPrint.document.writeln(numchars+"<BR/>");
//	}

// This function is passed a DOM Node object and checks to see if that node
// represents an HTML tag; i.e., if the node is an Element object. It
// recursively calls itself on each of the children of the node, testing
// them in the same way. It returns the total number of Element objects
// it encounters. If you invoke this function by passing it the
// Document object, it traverses the entire DOM tree.
function countTags(n) {

    //outPrint = window.open("","output")

    //if (!window.Node) {
    var Node = {            // If there is no Node object, define one
        ELEMENT_NODE: 1,    // with the following properties and values.
        ATTRIBUTE_NODE: 2,  // Note that these are HTML node types only.
        TEXT_NODE: 3,       // For XML-specific nodes, you need to add
        COMMENT_NODE: 8,    // other constants here.
        DOCUMENT_NODE: 9,
        DOCUMENT_FRAGMENT_NODE: 11
    }
    //}


    // n is a Node

    var element_numtags = 0;
    // Initialize the tag counter
    //var document_numtags = 0;
    var thisValue;

    if (n.nodeType == 1 /*Node.ELEMENT_NODE*/) {
        // Check if n is an Element

        thisValue = n.nodeName;
        //alert(n.nodeName)
        //element_numtags++;                          // Increment the counter if so
        var element_children = n.childNodes;
        // Now get all children of n
        //for(var i=0; i < element_children.length; i++) {    // Loop through the children
        //	element_numtags+= countTags(element_children[i]);      // Recurse on each one
        //}
        for (var m = n.firstChild; m != null; m = m.nextSibling) {
            //	 element_numtags+= countTags(m);
            thisValue = countTags(m);
        }
    }

    //else if (n.nodeType == 9 /*Node.DOCUMENT_NODE*/) {
    // Check if n is an Element


    //	document_numtags++;                              // Increment the counter if so
    //	var document_children = n.childNodes;                // Now get all children of n
    //	for(var i=0; i < document_children.length; i++) {    // Loop through the children
    //		document_numtags+= countTags(document_children[i]);      // Recurse on each one
    //	}
    //return document_numtags;
    //}
    //return numtags;                             // Return the total number of tags
    //return elementName;


    //var element_out = new Array("element ", element_numtags)
    var element_out = new Array("element ", thisValue)

    //var document_out = new Array("document: ", document_numtags)
    var nodes = new Array(element_out);


    //var out = "Nodes \n";
    for (i in nodes) {
        //outPrint.document.writeln((nodes[i]+"<BR/>"));
        if (nodes[i] != 0) {
            for (j in nodes[i]) {

                if (nodes[i][j] != 0) {
                    debug(nodes[i][j]);
                }
            }
        }
    }

    //	if (n.nodeType == 1 /*Node.TEXT_NODE*/) {
    // Check if n is a Text object
    //                  outPrint.document.writeln(n.length+"<BR/>");                     // If so, return its length
    // Otherwise, n may have children whose characters we need to count
    //          	var numchars = 0;  // Used to hold total characters of the children
    // Instead of using the childNodes property, this loop examines the
    // children of n using the firstChild and nextSibling properties.
    //         	for(var m = n.firstChild; m != null; m = m.nextSibling) {
    //                 	numchars += countCharacters(m);  // Add up total characters found
    //         	}
    //return numchars;                     // Return total characters
    //          	outPrint.document.writeln(numchars+"<BR/>");
    //     }
    //return out;
}


function traverse() {
    var info = "Traverse DOM \n"
    //+ describeDocument(parent.contentFRM.document);
    for (z = 0; z < window.frames['contentFRM'].document.getElementById('theBody').elements.length; ++z) {
        //	while(window.document.childNodes[z] != undefined) {
        + window.frames['contentFRM'].document.getElementById('theBody').elements[z] + "\n"
        //for(x=0; x<window.document.childNodes[z].childNodes.length; ++x) {
        //	while(window.document.childNodes[z].childNodes[x] != null) {
        //		+ window.document.childNodes[z].childNodes[x] + "\n"
        //		for(y=0;y<window.document.childNodes[z].childNodes[x].childNodes.length; ++y) {
        //			+window.document.childNodes[z].childNodes[x].childNodes[y]+ "\n"
        //		}
        //	}
        //}
        //	}
    }
    ;
    alert(info)
}

function describeElement(element) {
    //outPrint.document.write("<H4> Element </H4>")
    var eName = element.name;
    alert(eName)
    //if (window.frames['contentFRM'].document.getElementsByName(eName)!= undefined) describeElementsByName(eName)
    for (k = 0; k < document.getElementsByName(eName).elements.length; ++k) {
        getFormElementInfo(document.getElementsByName(eName).elements[k])
    }
}

function describeDocument(doc) {
            + " ContentWindow: " + doc.contentWindow + "\n"
            + " bgColor: " + doc.bgColor + "\n"
            + " body: " + doc.body + "\n"
            + " doctype: " + doc.doctype + "\n"
            + " characterSet: " + doc.characterSet + "\n"
            + " referrer: " + doc.referrer + "\n"
            + " title: " + doc.title + "\n"
            + " url: " + doc.URL + "\n"
            + " namespaceURI: " + doc.namespaceURI + "\n"
            + " location: " + doc.location + "\n"
            + " documentElement: " + doc.documentElement + "\n"
            + " domain: " + doc.domain + "\n"
            + " DOM implementation: " + doc.implementation + "\n"
            + " cookie: " + doc.cookie + "\n"
            + " defaultView: " + doc.defaultView + "\n"
            + " lastModified: " + doc.lastModified + "\n"
    //describeLinks()
    //describeForms()
}

// Debug
var outputWindow 
function debug() {
    outputWindow = window.open('' + self.location, 'output', 'left=20, top=20,width=500,toolbar=1,resizable=1');
    setupWindow()
    describeBrowser()
    describeWindow()
    describeDocument()
}

function setupWindow() {

    outputWindow.document.write("<HTML><HEAD><TITLE>Output Window</TITLE></HEAD><BODY>")
}

function describeBrowser() {

    outputWindow.document.write("<H2>Browser properties</H2>")
    outputWindow.document.write(navigator.appCodeName+" ")
    outputWindow.document.write(navigator.appName+ " ")
    outputWindow.document.write(navigator.appVersion+ "<BR>")
    outputWindow.document.write(navigator.mimeTypes.length+" MIME types are defiened. ")
    outputWindow.document.write(navigator.plugins.length+" plugins are installeded. ")
}

function describeWindow() {
    outputWindow.document.write("<H2>Window Properties</H2>")
    outputWindow.document.write("Frames: " +frames.length+"<BR>")
    outputWindow.document.write("URL: "+location.href+"<BR>")
}

function describeDocument() {
   outputWindow.document.write("<H2>Document Properties</H2>")
   outputWindow.document.write(" ContentWindow: " + document.DOCUMENT_NODE.contentWindow + "<BR>"
            + " bgColor: " + document.DOCUMENT_NODE.bgColor + "<BR>"
            + " body: " + document.DOCUMENT_NODE.body + "<BR>"
            + " doctype: " + document.DOCUMENT_NODE.doctype + "<BR>"
            //+ " characterSet: " + document.DOCUMENT_NODE.characterSet + "\n"
            + " referrer: " + document.DOCUMENT_NODE.referrer + "<BR>"
            + " title: " + document.DOCUMENT_NODE.title + "<BR>"
            + " url: " + document.DOCUMENT_NODE.URL + "<BR>"
            + " namespaceURI: " + document.DOCUMENT_NODE.namespaceURI + "<BR>"
            + " location: " + document.DOCUMENT_NODE.location + "<BR>"
            + " documentElement: " + document.DOCUMENT_NODE.documentElement + "<BR>"
            + " domain: " + document.DOCUMENT_NODE.domain + "<BR>"
            + " DOM implementation: " + document.DOCUMENT_NODE.implementation + "<BR>"
            + " cookie: " + document.DOCUMENT_NODE.cookie + "<BR>"
            + " defaultView: " + document.DOCUMENT_NODE.defaultView + "<BR>"
            //+ " lastModified: " + document.DOCUMENT_NODE.lastModified + "\n"
    )
    describeLinks()
    describeForms()
}

function describeLinks(){
    outputWindow.document.write("<H3>Links</H3>")
    outputWindow.document.write("This document contains " +document.links.length+" links: <BR>")
    for(i=0;i<document.links.length;++i)
            outputWindow.document.write(document.links[i].href+"<BR>")
}

function describeForms() {
    outputWindow.document.write("<H3>Forms</H3>")
    for(i=0;i<document.forms.length;++i) describeForm(i)
}


function describeForm(n) {

    outputWindow.document.write("Form " +n+ " has following" + document.forms[n].elements.length+ " elements: ")
    for (j=0;j<document.forms[n].elements.length;++j)
        outputWindow.document.write(" name=" + document.forms[n].elements[j].name + " value=" + document.forms[n].elements[j].value)
    outputWindow.document.write("<BR>")
}

function finishWindow() {
    outputWindow.document.write("<FORM><INPUT Type='button' Value='Close Window' onClick='window.close()'></FORM>")
    outputWindow.document.write("</BODY></HTML>")
}
