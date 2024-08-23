import { ArrowRight, Calendar, MapPin, Settings2, X } from "lucide-react";
import { Button } from "../../components/button";
import { useParams } from "react-router-dom";
import { api } from "../../lib/axios";
import { useEffect, useState } from "react";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import { DateRange, DayPicker } from "react-day-picker";

interface Trip {
  id: string;
  destination: string;
  startsAt: string;
  endsAt: string;
  isConfirmed: boolean;
  ownerName: string;
  ownerEmail: string;
}

export function DestinationAndDateHeader() {
  const { tripID } = useParams();
  const [trip, setTrip] = useState<Trip | undefined>();

  const [changeTrip, setChangeTrip] = useState(false);
  const [isDatePickerOpen, setIsDatePickerOpen] = useState(false);

  const [newDestination, setNewDestination] = useState("");
  const [newEventStartAndEndDates, setNewEventStartAndEndDates] = useState<DateRange | undefined>();

  function openTrip() {
    setChangeTrip(true);
  }

  function closeTrip() {
    setChangeTrip(false);
  }

  function openDatePicker() {
    setIsDatePickerOpen(true);
  }

  function closeDatePicker() {
    setIsDatePickerOpen(false);
  }

  async function updateTrip() {
    closeTrip();

    if (newDestination && newEventStartAndEndDates) {
      const response = await api.put(`/trips/${trip?.id}`, {
        destination: newDestination,
        starts_at: newEventStartAndEndDates?.from,
        ends_at: newEventStartAndEndDates?.to,
        owner_name: trip?.ownerName,
        owner_email: trip?.ownerEmail,
      });

      const { id } = response.data;
      if (id) api.get(`/trips/${tripID}`).then((response) => setTrip(response.data));
    }

    setNewDestination("");
    setNewEventStartAndEndDates(undefined);
  }

  useEffect(() => {
    api.get(`/trips/${tripID}`).then((response) => setTrip(response.data));
  }, [tripID]);

  const oldDisplayedDate = trip
    ? format(trip.startsAt, "d' de 'LLL", { locale: ptBR })
        .concat(" até ")
        .concat(format(trip.endsAt, "d' de 'LLL", { locale: ptBR }))
    : null;

  const newDisplayedDate =
    newEventStartAndEndDates && newEventStartAndEndDates.from && newEventStartAndEndDates.to
      ? format(newEventStartAndEndDates.from, "d' de 'LLL", { locale: ptBR })
          .concat(" até ")
          .concat(format(newEventStartAndEndDates.to, "d' de 'LLL", { locale: ptBR }))
      : "Quando?";

  return (
    <div className="px-4 h-16 rounded-xl bg-zinc-900 flex items-center justify-between">
      <div className="flex items-center gap-2">
        <MapPin className="size-5 text-zinc-400" />
        {changeTrip ? (
          <input
            disabled={!changeTrip}
            type="text"
            value={newDestination}
            placeholder="Para onde você vai?"
            className="bg-transparent placeholder-zinc-400 text-zinc-400 outline-none flex-1"
            onChange={(event) => setNewDestination(event.target.value)}
          />
        ) : (
          <span className="text-zinc-100">{trip?.destination}</span>
        )}
      </div>

      <div className="flex items-center gap-5">
        <div className="flex items-center gap-2 flex-1">
          <Calendar className="size-5 text-zinc-400" />
          {changeTrip ? (
            <button onClick={openDatePicker}>
              <span className="text-zinc-400">{newDisplayedDate}</span>
            </button>
          ) : (
            <span className="text-zinc-100">{oldDisplayedDate}</span>
          )}
        </div>

        <div className="w-px h-6 bg-zinc-800"></div>

        {changeTrip ? (
          <Button onClick={updateTrip} variant="primary">
            Continuar
            <ArrowRight className="size-5" />
          </Button>
        ) : (
          <Button onClick={openTrip} variant="secondary">
            Alterar local/data
            <Settings2 className="size-5" />
          </Button>
        )}

        {isDatePickerOpen && (
          <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
            <div className="rounded-xl py-5 px-5 bg-zinc-900 space-y-5">
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <h2 className="text-lg font-semibold">Selecione a data</h2>
                  <button>
                    <X className="size-5 text-zinc-400" onClick={closeDatePicker} />
                  </button>
                </div>
              </div>
              <DayPicker mode="range" selected={newEventStartAndEndDates} onSelect={setNewEventStartAndEndDates} />
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
