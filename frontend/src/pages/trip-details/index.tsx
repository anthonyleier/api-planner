import { Plus } from "lucide-react";
import { useCallback, useEffect, useState } from "react";
import { CreateActivityModal } from "./create-activity-modal";
import { ImportantLinks } from "./important-links";
import { Guests } from "./guests";
import { Activities } from "./activities";
import { DestinationAndDateHeader } from "./destination-and-date-header";
import { Button } from "../../components/button";
import { TripStatus } from "./trip-status";
import { useParams } from "react-router-dom";
import { api } from "../../lib/axios";

interface Activity {
  id: string;
  title: string;
  occursAt: string;
}

export function TripDetailsPage() {
  const { tripID } = useParams();
  const [activities, setActivities] = useState<Activity[]>([]);
  const [isCreateActivityModalOpen, setIsCreateActivityModalOpen] = useState(false);

  function openCreateActivityModal() {
    setIsCreateActivityModalOpen(true);
  }

  function closeCreateActivityModal() {
    setIsCreateActivityModalOpen(false);
  }

  const fetchActivities = useCallback(() => {
    api.get(`/trips/${tripID}/activities`).then((response) => setActivities(response.data));
  }, [tripID]);

  function handleActivityCreated() {
    fetchActivities();
  }

  useEffect(() => {
    fetchActivities();
  }, [fetchActivities, tripID]);

  return (
    <div className="max-w-6xl px-6 py-10 mx-auto space-y-8">
      <DestinationAndDateHeader />

      <main className="flex gap-16 px-4">
        {/* Left */}
        <div className="flex-1 space-y-6">
          <div className="flex items-center justify-between">
            <h2 className="text-3xl font-semibold">Atividades</h2>
            <Button onClick={openCreateActivityModal} variant="primary">
              <Plus className="size-5" />
              Cadastrar atividade
            </Button>
          </div>
          <Activities activities={activities} />
        </div>

        {/* Right */}
        <div className="w-80 space-y-6">
          <TripStatus />
          <div className="w-full h-px bg-zinc-800" />
          <ImportantLinks />
          <div className="w-full h-px bg-zinc-800" />
          <Guests />
        </div>
      </main>

      {isCreateActivityModalOpen && <CreateActivityModal closeCreateActivityModal={closeCreateActivityModal} onActivityCreated={handleActivityCreated} />}
    </div>
  );
}
