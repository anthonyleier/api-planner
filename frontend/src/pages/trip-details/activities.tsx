import { CircleCheck } from "lucide-react";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { api } from "../../lib/axios";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";

export interface Activity {
  id: string;
  title: string;
  occursAt: string;
}

export function Activities() {
  const { tripID } = useParams();
  const [activities, setActivities] = useState<Activity[]>([]);

  useEffect(() => {
    api.get(`http://localhost/trips/${tripID}/activities`).then((response) => setActivities(response.data));
  }, [tripID]);

  return (
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
  );
}
