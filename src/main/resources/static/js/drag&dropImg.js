// required elements
const dropArea = document.querySelector(".drag-area"),
      dragText = dropArea.querySelector("header"),
        button = document.getElementById("pickPhotoBtn"),
         input = document.getElementById("fotoUsuario");

let file; 
let fileCompressed; 

button.onclick = () => {
  input.click(); 
}

input.addEventListener("change", function() {
  file = this.files[0]; 
  dropArea.classList.add("dropped");
  showFile(); 
});

// dragover event
dropArea.addEventListener("dragover", (event) => {
  event.preventDefault(); 
  dropArea.classList.add("dropped");
  dragText.textContent = "Suelta para subir el archivo";
});

// dragleave event
dropArea.addEventListener("dragleave", () => {
  dropArea.classList.remove("dropped");
  dragText.textContent = "Arrastra una foto aquí";
});

// drop file event
dropArea.addEventListener("drop", (event)=>{
  event.preventDefault(); 
  file = event.dataTransfer.files[0];
  input.files = event.dataTransfer.files;
  showFile();
  //compressFile();
});

function showFile(){
  let fileType = file.type; 
  let validExtensions = ["image/jpeg", "image/jpg", "image/png"]; 
  
  if (validExtensions.includes(fileType)) { 
    let fileReader = new FileReader();

    fileReader.onload = ()=>{
      let fileURL = fileReader.result; 
      let imgTag = `<img src="${fileURL}" alt="imagen">`; 
      dropArea.innerHTML = imgTag; 
    }

    fileReader.readAsDataURL(file);

  } else {
    alert("¡Error! Formato de archivo no válido. Seleccione una imagen jpeg o png.");
    dropArea.classList.remove("dropped");
    dragText.textContent = "Arrastra una foto aquí";
  }
}

function compressFile(){
  let fileName = file.name.split('.')[0];
  let img = new Image();
  img.src = URL.createObjectURL(file);

  img.onload = function(){
      let canvas = document.createElement('canvas');
      canvas.width = img.width;
      canvas.height = img.height;
      let ctx = canvas.getContext('2d');
      ctx.drawImage(img, 0, 0);
      canvas.toBlob(function(blob){
              console.info(blob.size);
              fileCompressed = new File([blob], fileName + ".jpeg", {type: "image/jpeg"});
              //console.info(fileCompressed);
              //let xhr = new XMLHttpRequest();
               //let form = new FormData();
               //let form = document.getElementById('updateUserPhoto');
               //form.append("fotoUsuario", fileCompressed);
              // xhr.open("POST", "upload.php");
              // xhr.send(form);
      }, 'image/jpeg', 0.5);
  }
}