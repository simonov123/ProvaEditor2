console.log("DOCX.js caricato correttamente");

window.loadDOCXFromBase64 = async function(base64String) {
    try {
        // Decodifica base64 in array di byte
        const binary = atob(base64String);
        const len = binary.length;
        const bytes = new Uint8Array(len);
        for (let i = 0; i < len; i++) {
            bytes[i] = binary.charCodeAt(i);
        }

        const arrayBuffer = bytes.buffer;

        // Usa Mammoth per convertire DOCX → testo
        const result = await mammoth.convertToPlainText({ arrayBuffer: arrayBuffer });
        const text = result.value;

        document.getElementById("output").textContent = text;

        if (typeof window.sendTextToJava === "function") {
            const risposta = window.sendTextToJava(text);
            console.log("Risposta Java:", risposta);
        }

    } catch (err) {
        console.error("Errore nel parsing DOCX:", err);
        document.getElementById("output").textContent = "Errore nella lettura del file DOCX.";
    }
};
window.sendata = function () {
        const text = document.getElementById('output').textContent;
        if (typeof window.sendTextToJava === "function") {
            const result = window.sendTextToJava(text);
            console.log("Risposta da Java:", result);
        } else {
            console.warn("sendTextToJava non definita");
        }
    };
