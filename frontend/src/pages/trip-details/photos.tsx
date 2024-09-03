import { useCallback, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { api } from "../../lib/axios";
import { AddPhotoModal } from "./add-photo-modal";

interface Photo {
  url: string;
}

export function Photos() {
  const { tripID } = useParams();
  const [photos, setPhotos] = useState<Photo[]>([]);
  const [isAddPhotoModalOpen, setIsAddPhotoModalOpen] = useState(false);

  function openAddPhotoModal() {
    setIsAddPhotoModalOpen(true);
  }

  function closeAddPhotoModal() {
    setIsAddPhotoModalOpen(false);
  }

  const fetchPhotos = useCallback(() => {
    // api.get(`/trips/${tripID}/photos`).then((response) => setPhotos(response.data));
  }, [tripID]);

  function handleAddPhoto() {
    fetchPhotos();
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
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <img src="https://picsum.photos/200" className="w-full h-auto object-cover rounded-lg" />
        <button onClick={openAddPhotoModal} className="flex items-center justify-center w-full h-auto bg-zinc-800 hover:bg-zinc-700 cursor-pointer rounded-lg">
          <span className="text-4xl text-zinc-300">+</span>
        </button>
      </div>

      {isAddPhotoModalOpen && <AddPhotoModal closeAddPhotoModal={closeAddPhotoModal} onAddPhoto={handleAddPhoto} />}
    </>
  );
}
