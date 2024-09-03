import { ImportantLinks } from "./important-links";
import { Guests } from "./guests";
import { Activities } from "./activities";
import { DestinationAndDateHeader } from "./destination-and-date-header";
import { TripStatus } from "./trip-status";
import { Photos } from "./photos";

export function TripDetailsPage() {
  return (
    <div className="max-w-6xl px-6 py-10 mx-auto space-y-8">
      <DestinationAndDateHeader />

      <main className="flex gap-16 px-4">
        {/* Left */}
        <div className="flex-1 space-y-6">
          <Activities />
          <Photos />
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
    </div>
  );
}
