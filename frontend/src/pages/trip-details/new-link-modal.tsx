import { Link2, Tag, X } from "lucide-react";
import { Button } from "../../components/button";
import { FormEvent } from "react";
import { api } from "../../lib/axios";
import { useParams } from "react-router-dom";

interface NewLinkModalProps {
  closeNewLinkModal: () => void;
  onNewLink: () => void;
}

export function NewLinkModal({ closeNewLinkModal, onNewLink }: NewLinkModalProps) {
  const { tripID } = useParams();

  async function createLink(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const title = data.get("title")?.toString();
    const url = data.get("url")?.toString();

    await api.post(`/trips/${tripID}/links`, { title, url });

    closeNewLinkModal();
    onNewLink();
  }

  return (
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
      <div className="w-[640px] rounded-xl py-5 px-5 bg-zinc-900 space-y-5">
        <div className="space-y-2">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-semibold">Cadastrar link</h2>
            <button>
              <X className="size-5 text-zinc-400" onClick={closeNewLinkModal} />
            </button>
          </div>
          <p className="text-sm text-zinc-400">Todos os convidados podem visualizar os links.</p>
        </div>

        <form onSubmit={createLink} className="space-y-3">
          <div className="h-14 px-4 bg-zinc-950 border border-zinc-800 rounded-lg flex items-center gap-2">
            <Tag className="text-zinc-400 size-5" />
            <input name="title" placeholder="Qual o titulo?" className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1" />
          </div>

          <div className="h-14 px-4 bg-zinc-950 border border-zinc-800 rounded-lg flex items-center gap-2">
            <Link2 className="text-zinc-400 size-5" />
            <input name="url" placeholder="Qual a URL?" className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1" />
          </div>

          <Button variant="primary" size="full">
            Salvar link
          </Button>
        </form>
      </div>
    </div>
  );
}
