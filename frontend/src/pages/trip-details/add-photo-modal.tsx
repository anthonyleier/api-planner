import { Dropzone } from "../../components/dropzone";
import { X } from "lucide-react";

interface AddPhotoModalProps {
  closeAddPhotoModal: () => void;
  onAddPhoto: () => void;
}

export function AddPhotoModal({ closeAddPhotoModal, onAddPhoto }: AddPhotoModalProps) {
  return (
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
      <div className="w-[640px] rounded-xl py-5 px-5 bg-zinc-900 space-y-5">
        <div className="space-y-2">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-semibold">Upload de fotos</h2>
            <button>
              <X className="size-5 text-zinc-400" onClick={closeAddPhotoModal} />
            </button>
          </div>
          <p className="text-sm text-zinc-400">Todos os convidados podem visualizar e contribuir com fotos.</p>
        </div>

        <Dropzone onAddPhoto={onAddPhoto} />
      </div>
    </div>
  );
}
