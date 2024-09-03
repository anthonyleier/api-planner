import { CircleCheck, Plus } from "lucide-react";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import { Button } from "../../components/button";
import { useParams } from "react-router-dom";
import { api } from "../../lib/axios";
import { useCallback, useEffect, useState } from "react";
import { CreateActivityModal } from "./create-activity-modal";

interface Activity {
  id: string;
  title: string;
  occursAt: string;
}

export function Activities() {
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
    <>
      <div className="flex items-center justify-between">
        <h2 className="text-3xl font-semibold">Atividades</h2>
        <Button onClick={openCreateActivityModal} variant="primary">
          <Plus className="size-5" />
          Cadastrar atividade
        </Button>
      </div>
      <div className="space-y-8">
        <div className="space-y-2.5">
          {activities.map((activity) => (
            <div key={activity.id} className="space-y-2.5">
              <div className="px-4 py-2.5 bg-zinc-900 rounded-xl flex items-center gap-3">
                <CircleCheck className="size-5 text-lime-300" />
                <span className="text-zinc-100">{activity.title}</span>
                <span className="text-zinc-400 text-sm ml-auto">{format(activity.occursAt, "d 'de' MMMM 'às' HH:mm", { locale: ptBR })}</span>
              </div>
            </div>
          ))}
        </div>
      </div>

      {isCreateActivityModalOpen && <CreateActivityModal closeCreateActivityModal={closeCreateActivityModal} onActivityCreated={handleActivityCreated} />}
    </>
  );
}
