<?php
namespace App\DTO;

use App\Entity\Commande;
use Doctrine\Common\Collections\Collection;

class CommandeListDto {
    public int $id;
    public string $nomClient;
    public string $telephone;
    public Collection $commandeItems;
    public string $type;
    public string $etat;
    public ?\DateTimeImmutable $date;

    public static function toDto(Commande $commande): CommandeListDto {
        $dto = new CommandeListDto();
        $dto->id = $commande->getId();
        $dto->nomClient = $commande->getClient()?->getNomComplet() ?? '';
        $dto->telephone = $commande->getClient()?->getTelephone() ?? '';
        $dto->commandeItems = $commande->getCommandeItems()->map(function($item) {
            return CommandeItemDto::toDto($item);
        });
        $dto->type = is_object($commande->getType()) ? $commande->getType()->value : (string)$commande->getType();
        $dto->etat = is_object($commande->getEtat()) ? $commande->getEtat()->value : (string)$commande->getEtat();
        $dto->date = $commande->getDateCommande();
        return $dto;
    }

    public static function toDtoArray(array $commandes): array {
        return array_map(fn(Commande $c) => self::toDto($c), $commandes);
    }
}
