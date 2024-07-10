package br.com.anthonycruz.planner.link;

import br.com.anthonycruz.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {
    @Autowired
    LinkRepository repository;

    public LinkResponse registerLink(LinkRequestPayload payload, Trip trip) {
        Link newLink = new Link(payload.title(), payload.url(), trip);
        this.repository.save(newLink);
        return new LinkResponse(newLink.getId());
    }

    public List<LinkDTO> getAllLinksFromTrip(UUID id) {
        return this.repository.findByTripId(id).stream().map(link -> new LinkDTO(link.getId(), link.getTitle(), link.getUrl())).toList();

    }
}
