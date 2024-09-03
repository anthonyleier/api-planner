import { useCallback, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { api } from "../../lib/axios";
import { AddPhotoModal } from "./add-photo-modal";
import { X } from "lucide-react";

interface Photo {
  id: string;
  filename: string;
  url?: string;
}

export function Photos() {
  const { tripID } = useParams();
  const [photos, setPhotos] = useState<Photo[]>([]);
  const [isAddPhotoModalOpen, setIsAddPhotoModalOpen] = useState(false);
  const [hoveredIndex, setHoveredIndex] = useState<number | null>(null);

  function openAddPhotoModal() {
    setIsAddPhotoModalOpen(true);
  }

  function closeAddPhotoModal() {
    setIsAddPhotoModalOpen(false);
  }

  const fetchPhotos = useCallback(() => {
    api.get(`/trips/${tripID}/photos`).then((response) => setPhotos(response.data));
  }, [tripID]);

  function handleAddPhoto() {
    fetchPhotos();
  }

  function handleDeletePhoto(filename: string) {
    console.log(`Deletando ${filename} da viagem ${tripID}...`);
    api.delete(`/trips/${tripID}/photos/${filename}`).then(() => {
      setTimeout(() => {
        fetchPhotos();
      }, 500);
    });
  }

  useEffect(() => {
    fetchPhotos();
  }, [fetchPhotos, tripID]);

  return (
    <>
      <div className="flex items-center justify-between">
        <h2 className="text-3xl font-semibold">Fotos</h2>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
        {photos.map((photo, index) => {
          photo.url = `http://localhost:8080/trips/${tripID}/photos/${photo.filename}`;
          return (
            <div key={photo.id} className="relative w-full h-48 overflow-hidden group" onMouseEnter={() => setHoveredIndex(index)} onMouseLeave={() => setHoveredIndex(null)}>
              <a href={photo.url} target="_blank">
                <img src={photo.url} className="w-full h-full object-cover rounded-lg transition duration-300 ease-in-out group-hover:opacity-50" alt={`Photo ${photo.id}`} />
                <div className="absolute inset-0 bg-black opacity-0 transition-opacity duration-300 ease-in-out group-hover:opacity-30 rounded-lg"></div>
              </a>
              {hoveredIndex === index && (
                <button onClick={() => handleDeletePhoto(photo.filename)} className="absolute top-2 right-2 p-1">
                  <X />
                </button>
              )}
            </div>
          );
        })}
        <button onClick={openAddPhotoModal} className="flex items-center justify-center w-full h-48 bg-zinc-800 hover:bg-zinc-700 cursor-pointer rounded-lg">
          <span className="text-4xl text-zinc-300">+</span>
        </button>
      </div>

      {isAddPhotoModalOpen && <AddPhotoModal closeAddPhotoModal={closeAddPhotoModal} onAddPhoto={handleAddPhoto} />}
    </>
  );
}
