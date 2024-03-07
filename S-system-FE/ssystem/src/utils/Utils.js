/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

import {ref} from 'vue';
import { API } from "@/api";


function getValueByKey(data, key){
  const item = data.find(item => item.key === key);
  return item ? item.view : '';
};

function getExtension(data) {
  const splitData = data.split(".");
  const ext = splitData[splitData.length - 1];
  return ext;
};

function validCheckSearchInput(input) {
  var specialCharacterRegex = /[!@#$%^&*(),.?":{}|<>+=\[\]\\`]/;

  if(input == undefined) {
    input = "";
  }
  
  var values = Object.values(input);

  if (values.some(value => specialCharacterRegex.test(String(value)))) {
    alert("검색어에 특수문자는 넣으실 수 없습니다");
    return false;
  }

  return true;
}

function getFileIcon(extension) {
  switch (extension) {
    // Documents
    case 'pdf':
      return { icon: 'mdi-file-pdf-box', color: '#b30c00' };
    case 'hwp':
    case 'hwpx':
      return { icon: 'mdi-syllabary-hangul', color: '#35aee7' };
    case 'csv':
    case 'xls':
    case 'xlsx':
      return { icon: 'mdi-file-excel-box', color: '#1f7246' };
    case 'ppt':
    case 'pptx':
      return { icon: 'mdi-file-powerpoint-box', color: '#b74829' };
    case 'doc':
    case 'docx':
      return { icon: 'mdi-file-word-box', color: '#1079bf' };
  
    // Images
    case 'png':
    case 'jpg':
    case 'jpeg':
    case 'gif':
    case 'jfif':
    case 'bmp':
    case 'svg':
    case 'tiff':
    case 'wbmp':
    case 'webp':
      return { icon: 'mdi-file-image', color: '#03a9f4' };
  
    // Videos
    case 'mp4':
    case 'avi':
    case 'mkv':
    case 'mov':
    case 'wmv':
    case 'flv':
      return { icon: 'mdi-file-video', color: '#3f51b5' };
  
    // Audio
    case 'mp3':
    case 'wav':
    case 'ogg':
    case 'flac':
      return { icon: 'mdi-file-music', color: '#ff9800' };
  
    // Compressed files
    case 'zip':
    case 'rar':
    case '7z':
    case 'tar':
      return { icon: 'mdi-zip-box', color: '#607d8b' };
  
    // Text files
    case 'txt':
    case 'log':
    case 'md':
      return { icon: 'mdi-file-document', color: '#607d8b' };
  
    default:
      return { icon: 'mdi-file', color: '#000000' };
  }
  
};

// SHIELDViewer
const url = ref("");
async function viewDocument(file, userid, saveAuthArray, printAuth) {
  try {
    // 파일 입력
    let insertShieldViewerFormData = new FormData();
    const insertShieldViewerData = {
      userid: userid,
    };

    if(file.flepath !== undefined) {
      let fileInfo = {
        uploadPath: file.flepath,
        orgFilename: file.sfilename,
      };
      const res = await API.fileAPISbms.fileDownloadApi(fileInfo);
      insertShieldViewerFormData.append("files", new Blob([res.data], {type: "application/octet-stream"}), file.sfilename.replaceAll(" ", ""));
    }
    else {
      insertShieldViewerFormData.append("files", file.file);
    }

    insertShieldViewerFormData.append(
      "requiredInsertShieldViewer",
      new Blob([JSON.stringify(insertShieldViewerData)], {
        type: "application/json",
      })
    ); 
    
    const response = await API.dctAPI.registerDocByFile(insertShieldViewerFormData);
    const fid = response.data.result.file_info[0].fid;

    while(true) {
      const result = await API.dctAPI.checkDocConvert({fid: fid});
      if(result.data.result === 'Y') {
        url.value = await API.dctAPI.viewDocument(fid, btoa(saveAuthArray), btoa(printAuth));
        break;
      }
      await new Promise(resolve => setTimeout(resolve, 1000));
    }
  } catch(e) {
    console.error(e);
    alert("Server Error");
  }
};


const treeNodeColor = (node) => {
  const treeRow = document.getElementsByClassName("tree-row-item");
  const treeRowArray = Array.from(treeRow);
  treeRowArray.forEach((row) => {
    const spanElement = row.querySelector("span");
    if (spanElement && spanElement.textContent === node.label) {
      resetNodeColors();
      row.style.backgroundColor = "lavender";
    }
  });
};

const resetNodeColors = () => {
  const treeRowItem = document.getElementsByClassName("tree-row-item");
  for (let i = 0; i < treeRowItem.length; i++) {
    treeRowItem[i].style.backgroundColor = "";
  }
};

const handleMouseOver = (event) => {
  event.currentTarget.style.cursor = "pointer";
};

// ex) "전산팀-S123" -> "전산팀"
function trimAfterDash(str) {
  return str.split('-')[0];
};

// ex) "3/5" -> ["3", "5"]
function splitSlash(str) {
  var parts = str.split('/');
  return [parts[0], parts[1]];
};

export {
  getValueByKey,
  validCheckSearchInput,
  getExtension,
  getFileIcon,
  treeNodeColor,
  handleMouseOver,
  trimAfterDash,
  splitSlash,
  viewDocument,
  url
};