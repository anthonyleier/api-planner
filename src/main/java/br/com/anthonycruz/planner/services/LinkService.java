package br.com.anthonycruz.planner.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.anthonycruz.planner.dtos.LinkDTO;
import br.com.anthonycruz.planner.models.Link;
import br.com.anthonycruz.planner.models.Trip;
import br.com.anthonycruz.planner.repositories.LinkRepository;
import br.com.anthonycruz.planner.requests.LinkRequest;

@Service
public class LinkService {
    private final LinkRepository repository;

    public LinkService(LinkRepository repository) {
        this.repository = repository;
    }

    public Link registerLink(LinkRequest request, Trip trip) {
        Link newLink = new Link(request.title(), request.url(), trip);
        Link savedLink = this.repository.save(newLink);
        return savedLink;
    }

    public List<LinkDTO> getAllLinksFromTrip(UUID id) {
        return this.repository.findByTripId(id)
                .stream()
                .map(link -> new LinkDTO(link.getId(), link.getTitle(), link.getUrl()))
                .toList();
    }

    public Optional<Link> findById(UUID id) {
        return this.repository.findById(id);
    }

    public void delete(Link link) {
        this.repository.delete(link);
    }
}
