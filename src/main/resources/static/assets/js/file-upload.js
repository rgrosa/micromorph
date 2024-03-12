const form = document.getElementById("form");
const inputFile = document.getElementById("file");
const formData = new FormData();

const handleSubmit = (event) => {
  event.preventDefault();

let formdata = new FormData();
formdata.append("file", inputFile.files[0], inputFile.files[0].name);

let requestOptions = {
  method: 'POST',
  body: formdata,
  redirect: 'follow'
};

fetch(window.location.origin+"/micromorph/file", requestOptions)
  .then(response => {
            if(response.status != 201){
                window.alert("Alert - error on upload - please verify:  \n - File extension that is being sent")
            }else{
                window.location.reload();
            }
       }
  )
  .catch(error =>console.log('Error', error));
};

form.addEventListener("submit", handleSubmit);