import { CalendarCheck2, CalendarClock } from "lucide-react";
import { Button } from "../../components/button";
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { api } from "../../lib/axios";

interface Trip {
  id: string;
  destination: string;
  startsAt: string;
  endsAt: string;
  isConfirmed: boolean;
  ownerName: string;
  ownerEmail: string;
}

export function TripStatus() {
  const { tripID } = useParams();
  const [trip, setTrip] = useState<Trip | undefined>();

  useEffect(() => {
    api.get(`/trips/${tripID}`).then((response) => setTrip(response.data));
  }, [tripID]);

  function confirmTrip() {
    api.get(`/trips/${tripID}/confirm`).then((response) => setTrip(response.data));
  }

  if (!trip?.isConfirmed) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-between gap-4">
          <h2 className="font-semibold text-xl">Viagem em planejamento</h2>
          <CalendarClock className="text-zinc-400 size-5 shrink-0" />
        </div>
        <Button onClick={confirmTrip} variant="secondary" size="full">
          <CalendarCheck2 className="size-5" />
          Confirmar viagem
        </Button>
      </div>
    );
  }

  if (trip?.isConfirmed) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-between gap-4">
          <h2 className="font-semibold text-xl">Viagem confirmada</h2>
          <CalendarCheck2 className="text-lime-300 size-5 shrink-0" />
        </div>
      </div>
    );
  }
}
