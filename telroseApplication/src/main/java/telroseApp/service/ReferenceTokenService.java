package telroseApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telroseApp.repository.ReferenceTokenRepository;
import telroseApp.model.ReferenceToken;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReferenceTokenService {

    private final ReferenceTokenRepository referenceTokenRepository;


    public ReferenceToken generateReferenceTokenByCostumerId(Long costumerId)
    {
        var referenceToken = ReferenceToken.builder().token(UUID.randomUUID().toString())
                .costumerId(costumerId)
                .build();

        return referenceTokenRepository.save(referenceToken);
    }


}
