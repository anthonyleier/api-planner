import { Link2, Tag, X } from "lucide-react";
import { Button } from "../../components/button";
import { ChangeEvent, FormEvent, useState } from "react";
import { api } from "../../lib/axios";
import { useParams } from "react-router-dom";

interface AddPhotoModalProps {
  closeAddPhotoModal: () => void;
  onAddPhoto: () => void;
}

export function AddPhotoModal({ closeAddPhotoModal, onAddPhoto }: AddPhotoModalProps) {
  const { tripID } = useParams();
  const [files, setFiles] = useState<File[]>([]);

  async function uploadPhoto(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    // const data = new FormData(event.currentTarget);

    // const title = data.get("title")?.toString();
    // const url = data.get("url")?.toString();

    // await api.post(`/trips/${tripID}/links`, { title, url });

    closeAddPhotoModal();
    // onAddPhoto();
  }

  function onFilesSelected(files: FileList) {
    console.log(files);
  }

  const [isDragOver, setIsDragOver] = useState(false);

  const handleDragOver = (e: DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    setIsDragOver(true);
  };

  const handleDragLeave = () => {
    setIsDragOver(false);
  };

  const handleDrop = (e: DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    setIsDragOver(false);
    if (e.dataTransfer.files.length) {
      onFilesSelected(e.dataTransfer.files);
    }
  };

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      onFilesSelected(e.target.files);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
      <div className="w-[640px] rounded-xl py-5 px-5 bg-zinc-900 space-y-5">
        <div className="space-y-2">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-semibold">Upload foto</h2>
            <button>
              <X className="size-5 text-zinc-400" onClick={closeAddPhotoModal} />
            </button>
          </div>
          <p className="text-sm text-zinc-400">Todos os convidados podem visualizar e contribuir com fotos.</p>
        </div>

        {/* <div
          className="flex flex-col items-center justify-center w-full h-64 p-4 border-2 border-dashed border-gray-400 rounded-lg"
          onDrop={handleDrop}
          onDragOver={handleDragOver}
        >
          <p className="text-gray-600">Arraste e solte os arquivos aqui ou clique para selecionar</p>
          <input
            type="file"
            multiple
            onChange={(e) => setFiles([...files, ...e.target.files])}
            className="hidden"
            id="fileInput"
          />
          <label htmlFor="fileInput" className="mt-2 text-blue-500 cursor-pointer">
            Selecione os arquivos
          </label>
          <ul className="mt-4 text-gray-600">
            {files.length > 0 &&
          Array.from(files).map((file, index) => (
            <li key={index}>{file.name}</li>
          ))}
          </ul>
        </div> */}

        <div
          className={`border-2 border-dashed p-4 text-center ${isDragOver ? "border-blue-500" : "border-gray-300"} cursor-pointer`}
          onDragOver={handleDragOver}
          onDragLeave={handleDragLeave}
          onDrop={handleDrop}
        >
          <input type="file" multiple onChange={handleChange} className="hidden" />
          <p>Arraste e solte arquivos aqui ou clique para selecionar</p>
        </div>
      </div>
    </div>
  );
}
