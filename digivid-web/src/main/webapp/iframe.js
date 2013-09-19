

var IFrameObj; // our IFrame object

function buildQueryString(theFormName) {
  theForm = document.forms[theFormName];
  var qs = ''
  for (e=0;e<theForm.elements.length;e++) {
    if (theForm.elements[e].name!='') {
      qs+=(qs=='')?'?':'&'
      qs+=theForm.elements[e].name+'='+escape(theForm.elements[e].value)
      }
    }
  return qs
}

function callToServer() {
  if (!document.createElement) {return true};
  var IFrameDoc;
  var URL = 'server.html';
  if (!IFrameObj && document.createElement) {
    // create the IFrame and assign a reference to the
    // object to our global variable IFrameObj.
    // this will only happen the first time
    // callToServer() is called
   try {
      var tempIFrame=document.createElement('iframe');
      tempIFrame.setAttribute('id','contentFRM');
      tempIFrame.style.border='0px';
      tempIFrame.style.width='0px';
      tempIFrame.style.height='0px';
      IFrameObj = document.body.appendChild(tempIFrame);

      if (document.frames) {
        // this is for IE5 Mac, because it will only
        // allow access to the document object
        // of the IFrame if we access it through
        // the document.frames array
        IFrameObj = document.frames['contentFRM'];
      }
    } catch(exception) {
      // This is for IE5 PC, which does not allow dynamic creation
      // and manipulation of an iframe object. Instead, we'll fake
      // it up by creating our own objects.
      iframeHTML='\<iframe id="contentFRM" style="';
      iframeHTML+='border:0px;';
      iframeHTML+='width:0px;';
      iframeHTML+='height:0px;';
      iframeHTML+='"><\/iframe>';
      document.body.innerHTML+=iframeHTML;
      IFrameObj = new Object();
      IFrameObj.document = new Object();
      IFrameObj.document.location = new Object();
      IFrameObj.document.location.iframe = document.getElementById('contentFRM');
      IFrameObj.document.location.replace = function(location) {
        this.iframe.src = location;
      }
    }
  }

  if (navigator.userAgent.indexOf('Gecko') !=-1 && !IFrameObj.contentDocument) {
    // we have to give NS6 a fraction of a second
    // to recognize the new IFrame
    setTimeout('callToServer()',10);
    return false;
  }

  if (IFrameObj.contentDocument) {
    // For NS6
    IFrameDoc = IFrameObj.contentDocument;
  } else if (IFrameObj.contentWindow) {
    // For IE5.5 and IE6
    IFrameDoc = IFrameObj.contentWindow.document;
  } else if (IFrameObj.document) {
    // For IE5
    IFrameDoc = IFrameObj.document;
  } else {
    return true;
  }

  IFrameDoc.location.replace(URL);
  return false;
}


// handleResponse is passed two parameters when called from the onload
// event of the pages loaded in the hidden IFRAME:
//	st: a string indicating which state is being loaded
//	doc: the document object of the page loaded in the IFRAME
function handleResponse(st, doc) {
	// get a reference to the multiple select list, which we will populate
	// with the data from the document loaded in the IFRAME
    //var namesEl = document.forms.stateForm.zipNames
//    var namesEl = document.forms.stateForm.encoderName

	// clear earlier records from the multiple select list
//	namesEl.length = 0

	// get a reference to the DIV containing the data for this state
//	var dataEl = doc.getElementById(st)

	// get a reference to the collection of the children elements of
	// our DIV containing the data (this collection is the DIVs containing
	// the actual zip names)
//	namesColl = dataEl.childNodes

	// for easy scripting, assign the number of ZIP names for this state
	// to a variable
//	var numNames = namesColl.length

	// iterate through the collection of zip Names and
	// create an option element for each one
//	for (var q=0; q<numNames; q++) {
//		if (namesColl[q].nodeType!=1) continue; // it's not an element node, let's skedaddle
//		var str = '' // used to store the text we'll use in the new option
//		str+= namesColl[q].id + ' ('

		// get a reference to the collection of the children elements of
		// this DIV (this collection contains the zip codes that fall under this zip name)

//		var zipsColl = doc.getElementById(namesColl[q].id).childNodes
//		var numZips = zipsColl.length

		// iterate through this collection of zips and each one to the string
//		for (var r=0; r<numZips; r++) {
//			if (zipsColl[r].nodeType!=1) continue; // it's not an element node, let's skedaddle
//			str += zipsColl[r].id + ' '
//			}
//		str+= ')'

		// create a new option element and add it to the zipNames form element
//		newOption = new Option(str)
//		namesEl.options[namesEl.length] = newOption
//		}

	//provide a "success" message
	var responseMessage = document.getElementById('responseMessage');
    //var responseMessage = document.getElementById('contentLayer');
    responseMessage.innerHTML = 'loaded records from <a href="'+doc.location+'">this external file<\/a>.';

}


