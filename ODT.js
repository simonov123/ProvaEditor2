console.log("ODT.js caricato correttamente");
window.loadODTFromBase64 = async function(base64String) {
    try {
        const binary = atob(base64String);
        const len = binary.length;
        const bytes = new Uint8Array(len);
        for (let i = 0; i < len; i++) {
            bytes[i] = binary.charCodeAt(i);
        }

        const zip = await JSZip.loadAsync(bytes);
        const contentXML = await zip.file("content.xml").async("string");

        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(contentXML, "text/xml");
        const paragraphs = xmlDoc.getElementsByTagName("text:p");

        let output = "";
        for (let p of paragraphs) {
            output += p.textContent + "\n";
        }

        document.getElementById("output").textContent = output;
        

    } catch (err) {
        console.error("Errore nel parsing ODT:", err);
        document.getElementById("output").textContent = "Errore nella lettura del file ODT.";
    }
};
function sendata(){
	var text=document.getElementById('output').textContent;
	  const result = window.sendTextToJava(text);
	   console.log("Risposta da Java:", result);
	
}