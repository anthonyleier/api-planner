import { useDropzone } from "react-dropzone";
import { api } from "../lib/axios";
import { useParams } from "react-router-dom";

interface Dropzone {
  onAddPhoto: () => void;
}

export function Dropzone({ onAddPhoto }: Dropzone) {
  const { tripID } = useParams();

  const onDrop = async (files: File[]) => {
    for (const file of files) {
      const formData = new FormData();
      formData.append("file", file);

      try {
        await api.post(`/trips/${tripID}/photos`, formData, { headers: { "Content-Type": "multipart/form-data" } });
        console.log(`Arquivo ${file.name} enviado com sucesso.`);
        onAddPhoto();
      } catch (error) {
        console.error(`Erro ao enviar o arquivo ${file.name}:`, error);
      }
    }
  };

  const { getRootProps, getInputProps } = useDropzone({ onDrop, multiple: true });

  return (
    <div {...getRootProps()} className="border-2 border-dashed border-zinc-100 h-24 p-4 rounded-lg flex flex-col items-center justify-center cursor-pointer">
      <input {...getInputProps()} />
      <p className="text-zinc-400">Arraste e solte arquivos aqui, ou clique para selecionar</p>
    </div>
  );
}
