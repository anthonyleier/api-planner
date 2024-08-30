import { CheckCircle2, CircleDashed, UserPlus } from "lucide-react";
import { Button } from "../../components/button";
import { api } from "../../lib/axios";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { InviteParticipantModal } from "./invite-participant-modal";

interface Participant {
  id: string;
  name: string;
  email: string;
  isConfirmed: boolean;
}

export function Guests() {
  const { tripID } = useParams();
  const [participants, setParticipants] = useState<Participant[]>([]);
  const [isInviteParticipantModalOpen, setIsInviteParticipantModalOpen] = useState(false);

  function openInviteParticipantModal() {
    setIsInviteParticipantModalOpen(true);
  }

  function closeInviteParticipantModal() {
    setIsInviteParticipantModalOpen(false);
  }

  useEffect(() => {
    api.get(`/trips/${tripID}/participants`).then((response) => setParticipants(response.data));
  }, [tripID]);

  return (
    <div className="space-y-6">
      <h2 className="font-semibold text-xl">Convidados</h2>

      <div className="space-y-5">
        {participants.map((participant, index) => (
          <div key={participant.id} className="flex items-center justify-between gap-4">
            <div className="space-y-1.5">
              {participant.name}
              <span className="block font-medium text-zinc-100">{participant.name !== "" ? participant.name : `Convidado ${index + 1}`}</span>
              <span className="block text-sm text-zinc-400 truncate">{participant.email}</span>
            </div>
            {participant.isConfirmed ? <CheckCircle2 className="text-lime-300 size-5 shrink-0" /> : <CircleDashed className="text-zinc-400 size-5 shrink-0" />}
          </div>
        ))}
      </div>

      <Button onClick={openInviteParticipantModal} variant="secondary" size="full">
        <UserPlus className="size-5" />
        Convidar participante
      </Button>

      {isInviteParticipantModalOpen && <InviteParticipantModal closeInviteParticipantModal={closeInviteParticipantModal} />}
    </div>
  );
}
