import { ref } from 'vue';
import { Quill } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';

const textChanged = ref(false);

const fontSizeArr = [ '11px', '13px','16px','20px','24px','32px','42px','54px','68px','84px' ];
const fontArr = ['sans-serif', 'serif', 'monospace'];
const setQuillEditor = (param, getData) => {
  // custom size select button
  let Size = Quill.import('attributors/style/size');
  Size.whitelist = fontSizeArr;
  Quill.register(Size, true);

  // custom font select button
  let Font = Quill.import('formats/font');
  Font.whitelist = fontArr;
  Quill.register(Font, true);

  // input Quill contents
  let quill = new Quill('#quill-editor', options);
  if(getData === true) {
    if(param.cont !== "")
      quill.setContents(JSON.parse(param.cont).ops);
  }

  quill.on(
    'text-change', 
    function update() {
      textChanged.value = true;
      param.cont = quill.getContents();
      return param;
    }
  );
};

const toolbar = [
  [
    { 'font': ['sans-serif', 'serif', 'monospace'] },
    { 'size': ['11px','13px','16px','20px','24px','32px','42px','54px','68px','84px'] }
  ],
  ["bold", "italic", "underline", "strike"],
  [{ color: [] }, { background: [] }, "blockquote"],
  [
    { 'list': "ordered" },
    { 'list': "bullet" },
    { 'align': [] },
  ],
  ["link"],
  ["clean"],
];

const options = {
  modules: {
    toolbar: toolbar
  },
  placeholder: '내용을 입력하세요.',
  theme: 'snow',
};


// for detail page
const setQuillEditorForDetail = (param) => {
  // custom size select button
  let Size = Quill.import('attributors/style/size');
  Size.whitelist = fontSizeArr;
  Quill.register(Size, true);

  // custom font select button
  let Font = Quill.import('formats/font');
  Font.whitelist = fontArr;
  Quill.register(Font, true);

  let quill = new Quill('#quill-editor', optionsForDetail);
  if(param.cont !== "")
    quill.setContents(JSON.parse(param.cont).ops);
};

const optionsForDetail = {
    modules: {
      toolbar: false
    },
    readOnly: true,
    theme: 'snow'
};

export { textChanged, setQuillEditor, setQuillEditorForDetail };