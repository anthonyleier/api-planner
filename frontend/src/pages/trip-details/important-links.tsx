import { CircleX, Link2, Plus } from "lucide-react";
import { Button } from "../../components/button";
import { useCallback, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { api } from "../../lib/axios";
import { NewLinkModal } from "./new-link-modal";

interface Link {
  id: string;
  title: string;
  url: string;
}

export function ImportantLinks() {
  const { tripID } = useParams();
  const [links, setLinks] = useState<Link[]>([]);
  const [isNewLinkModalOpen, setIsNewLinkModalOpen] = useState(false);
  const [hoveredIndex, setHoveredIndex] = useState<number | null>(null);

  function openNewLinkModal() {
    setIsNewLinkModalOpen(true);
  }

  function closeNewLinkModal() {
    setIsNewLinkModalOpen(false);
  }

  const fetchLinks = useCallback(() => {
    api.get(`/trips/${tripID}/links`).then((response) => setLinks(response.data));
  }, [tripID]);

  function handleNewLink() {
    fetchLinks();
  }

  function deleteLink(link: Link) {
    api.delete(`/links/${link.id}`).then(() => {
      setTimeout(() => {
        fetchLinks();
      }, 500);
    });
  }

  useEffect(() => {
    fetchLinks();
  }, [fetchLinks, tripID]);

  return (
    <div className="space-y-6">
      <h2 className="font-semibold text-xl">Links Importantes</h2>

      <div className="space-y-5">
        {links.map((link, index) => (
          <div key={link.id} className="flex items-center justify-between gap-4">
            <div className="space-y-1.5">
              <span className="block font-medium text-zinc-100">{link.title}</span>
              <a href={link.url} target="_blank" className="block text-xs text-zinc-400 hover:text-zinc-200 truncate">
                {link.url}
              </a>
            </div>
            <div onMouseEnter={() => setHoveredIndex(index)} onMouseLeave={() => setHoveredIndex(null)}>
              {hoveredIndex === index ? <CircleX onClick={() => deleteLink(link)} className="text-red-400 size-5 shrink-0" /> : <Link2 className="text-zinc-400 size-5 shrink-0" />}
            </div>
          </div>
        ))}
      </div>

      <Button onClick={openNewLinkModal} variant="secondary" size="full">
        <Plus className="size-5" />
        Cadastrar novo link
      </Button>

      {isNewLinkModalOpen && <NewLinkModal closeNewLinkModal={closeNewLinkModal} onNewLink={handleNewLink} />}
    </div>
  );
}
