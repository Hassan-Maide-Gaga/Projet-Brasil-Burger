<?php

namespace App\Entity;

use App\Repository\UserRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use Symfony\Component\Security\Core\User\UserInterface;

#[ORM\Entity(repositoryClass: UserRepository::class)]
#[ORM\Table(name: '`user`')]
class User implements UserInterface, PasswordAuthenticatedUserInterface
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    private ?string $nomComplet = null;

    #[ORM\Column(length: 255)]
    private ?string $telephone = null;

    #[ORM\Column(length: 255)]
    private ?string $email = null;

    #[ORM\Column(length: 255)]
    private ?string $password = null;

    #[ORM\Column(length: 255, enumType: RoleUser::class)]
    private ?RoleUser $role = null;

    #[ORM\Column]
    private ?bool $etat = true;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNomComplet(): ?string
    {
        return $this->nomComplet;
    }

    public function setNomComplet(string $nomComplet): static
    {
        $this->nomComplet = $nomComplet;
        return $this;
    }

    public function getTelephone(): ?string
    {
        return $this->telephone;
    }

    public function setTelephone(string $telephone): static
    {
        $this->telephone = $telephone;
        return $this;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): static
    {
        $this->email = $email;
        return $this;
    }

    public function getPassword(): ?string
    {
        return $this->password;
    }

    public function setPassword(string $password): static
    {
        $this->password = $password;
        return $this;
    }

    public function getRole(): ?RoleUser
    {
        return $this->role;
    }

    public function setRole(RoleUser $role): static
    {
        $this->role = $role;
        return $this;
    }

    // Méthodes REQUISES par UserInterface
    public function getRoles(): array
    {
        // IMPORTANT : Doit retourner un tableau avec au moins 'ROLE_USER'
        $roles = ['ROLE_USER'];
        
        // Ajoute le rôle spécifique si existant
        if ($this->role) {
            if ($this->role instanceof RoleUser) {
                $roles[] = 'ROLE_' . $this->role->value;
            } elseif (is_string($this->role)) {
                $roles[] = 'ROLE_' . strtoupper($this->role);
            }
        }
        
        return array_unique($roles);
    }

    public function eraseCredentials(): void
    {
        // Si vous stockez des données sensibles temporaires, effacez-les ici
    }

    public function getUserIdentifier(): string
    {
        return (string) $this->email;
    }

    // Méthode pour la compatibilité (ancienne méthode)
    public function getUsername(): string
    {
        return $this->getUserIdentifier();
    }

    public function isEtat(): ?bool
    {
        return $this->etat;
    }

    public function setEtat(bool $etat): static
    {
        $this->etat = $etat;
        return $this;
    }
}