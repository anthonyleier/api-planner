import { CircleCheck } from "lucide-react";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";

interface Activity {
  id: string;
  title: string;
  occursAt: string;
}

interface ActivitiesProps {
  activities: Activity[];
}

export function Activities({ activities }: ActivitiesProps) {
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
