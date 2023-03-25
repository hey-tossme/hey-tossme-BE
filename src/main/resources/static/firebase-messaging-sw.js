importScripts('https://www.gstatic.com/firebasejs/5.9.2/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/5.9.2/firebase-messaging.js');

// Initialize Firebase
let firebaseConfig = {
  apiKey: "AIzaSyBLanhsR2zy6eOVCXgdCNvrsli_OO1N1_4",
  authDomain: "heytossme-ed900.firebaseapp.com",
  projectId: "heytossme-ed900",
  storageBucket: "heytossme-ed900.appspot.com",
  messagingSenderId: "487147260981",
  appId: "1:487147260981:web:598fde8581824b56dcf57c",
  measurementId: "G-GHXT3HQ94T"
};
firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();