<?php
namespace App\DTO;

use App\Entity\EtatCommande;
use App\Entity\TypeCommande;
use DateTime;

class CommandeSearchDto {
    public ?string $nomClient = null;
    public ?EtatCommande $etatCmd = null;
    public ?TypeCommande $typeCmd = null;
}