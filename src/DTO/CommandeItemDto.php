<?php
namespace App\DTO;

use App\Entity\CommandeItem;
use App\Entity\TypeCommandeItem;

class CommandeItemDto {
    public TypeCommandeItem $produit;
    public static function toDto(CommandeItem $item): self {
        $dto = new self();
        $dto->produit = $item->getType() ?? '';
        return $dto;
    }
}
