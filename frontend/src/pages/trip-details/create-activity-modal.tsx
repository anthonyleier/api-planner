import { Calendar, Tag, X } from "lucide-react";
import { Button } from "../../components/button";
import { FormEvent } from "react";
import { api } from "../../lib/axios";
import { useParams } from "react-router-dom";

interface CreateActivityModalProps {
  closeCreateActivityModal: () => void;
  onActivityCreated: () => void;
}

export function CreateActivityModal({ closeCreateActivityModal, onActivityCreated }: CreateActivityModalProps) {
  const { tripID } = useParams();

  async function createActivity(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const data = new FormData(event.currentTarget);
    const title = data.get("title")?.toString();
    const occurs_at = data.get("occurs_at")?.toString();

    await api.post(`/trips/${tripID}/activities`, { title, occurs_at });

    closeCreateActivityModal();
    onActivityCreated();
  }

  return (
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
      <div className="w-[640px] rounded-xl py-5 px-5 bg-zinc-900 space-y-5">
        <div className="space-y-2">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-semibold">Cadastrar atividade</h2>
            <button>
              <X className="size-5 text-zinc-400" onClick={closeCreateActivityModal} />
            </button>
          </div>
          <p className="text-sm text-zinc-400">Todos os convidados podem visualizar as atividades.</p>
        </div>

        <form onSubmit={createActivity} className="space-y-3">
          <div className="h-14 px-4 bg-zinc-950 border border-zinc-800 rounded-lg flex items-center gap-2">
            <Tag className="text-zinc-400 size-5" />
            <input name="title" placeholder="Qual a atividade?" className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1" />
          </div>

          <div className="flex-1 h-14 px-4 bg-zinc-950 border border-zinc-800 rounded-lg flex items-center gap-2">
            <Calendar className="text-zinc-400 size-5" />
            <input type="datetime-local" name="occurs_at" placeholder="Data e hora da atividade" className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1" />
          </div>

          <Button variant="primary" size="full">
            Salvar atividade
          </Button>
        </form>
      </div>
    </div>
  );
}
