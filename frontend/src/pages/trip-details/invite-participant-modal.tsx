import { Mail, X } from "lucide-react";
import { Button } from "../../components/button";
import { FormEvent } from "react";
import { api } from "../../lib/axios";
import { useParams } from "react-router-dom";

interface InviteParticipantModalProps {
  closeInviteParticipantModal: () => void;
  onParticipantInvited: () => void;
}

export function InviteParticipantModal({ closeInviteParticipantModal, onParticipantInvited }: InviteParticipantModalProps) {
  const { tripID } = useParams();

  async function inviteParticipant(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const email = data.get("email")?.toString();

    await api.post(`/trips/${tripID}/invite`, { email });

    closeInviteParticipantModal();
    onParticipantInvited();
  }

  return (
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
      <div className="w-[640px] rounded-xl py-5 px-5 bg-zinc-900 space-y-5">
        <div className="space-y-2">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-semibold">Convidar participante</h2>
            <button>
              <X className="size-5 text-zinc-400" onClick={closeInviteParticipantModal} />
            </button>
          </div>
          <p className="text-sm text-zinc-400">Todos os convidados podem visualizar os participantes.</p>
        </div>

        <form onSubmit={inviteParticipant} className="space-y-3">
          <div className="h-14 px-4 bg-zinc-950 border border-zinc-800 rounded-lg flex items-center gap-2">
            <Mail className="text-zinc-400 size-5" />
            <input name="email" placeholder="Qual o email?" className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1" />
          </div>

          <Button variant="primary" size="full">
            Convidar
          </Button>
        </form>
      </div>
    </div>
  );
}
